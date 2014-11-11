package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.net.URI;
import java.net.URISyntaxException;
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
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryInstructionHandler.class);

	private static IRecoveryCommunicator recoveryCommunicator;
	private static IP2PNetworkManager p2pNetworkManager;

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

	/**
	 * Handles an incoming instruction-message
	 * 
	 * @param instructionMessage
	 *            The incoming message
	 */
	public static void handleInstruction(PeerID sender, RecoveryInstructionMessage instructionMessage) {
		switch (instructionMessage.getMessageType()) {
		case RecoveryInstructionMessage.HOLD_ON:
			holdOn(instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.GO_ON:
			goOn(instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.ADD_QUERY:
			addQuery(instructionMessage.getPqlQuery(), instructionMessage.getSharedQueryId());
			break;
		case RecoveryInstructionMessage.UPDATE_RECEIVER:
			updateReceiver(instructionMessage.getNewSender(), instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.BE_BUDDY:
			beBuddy(sender, instructionMessage.getSharedQueryId(), instructionMessage.getPql());
			break;
		}
	}

	private static void holdOn(PipeID pipeId) {
		// Here we want to store the tuples
		RecoveryHelper.startBuffering(pipeId.toString());
	}

	private static void goOn(PipeID pipeId) {
		// Here we want to empty the buffer and go on sending the tuples to the
		// next peer
		RecoveryHelper.resumeSubscriptions(pipeId);
	}

	@SuppressWarnings("rawtypes")
	private static void addQuery(String pql, ID sharedQueryId) {
		
		if(!RecoveryCommunicator.getExecutor().isPresent()) {
			
			LOG.error("No executor bound!");
			return;
			
		}

		Collection<Integer> installedQueries = RecoveryHelper.installAndRunQueryPartFromPql(pql);

		// Call "receiveFromNewPeer" on the subsequent receiver so that that
		// peer creates a socket-connection to us
		IServerExecutor executor = RecoveryCommunicator.getExecutor().get();
		boolean foundReceiver = false;
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			if (installedQueries.contains(query.getID())) {
				for (IPhysicalOperator operator : query.getAllOperators()) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;

						// For this sender we want to get the peer to which
						// it sends to update the receiver on the other peer

						try {
							String peerIdString = sender.getPeerIDString();
							URI peerUri = new URI(peerIdString);
							PeerID peer = PeerID.create(peerUri);
							// To this peer we have to send an "UPDATE_RECEIVER"
							// message
							String pipeIdString = sender.getPipeIDString();
							URI pipeUri = new URI(pipeIdString);
							PipeID pipe = PipeID.create(pipeUri);

							PeerID ownPeerId = p2pNetworkManager.getLocalPeerID();

							recoveryCommunicator.sendUpdateReceiverMessage(peer, ownPeerId, pipe);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					} else if (operator instanceof JxtaReceiverPO) {
						foundReceiver = true;
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;

						try {
							String peerIdString = receiver.getPeerIDString();
							URI peerUri = new URI(peerIdString);
							PeerID peer = PeerID.create(peerUri);
							// To this peer we have to send an "GO_ON"
							// message
							String pipeIdString = receiver.getPipeIDString();
							URI pipeUri = new URI(pipeIdString);
							PipeID pipe = PipeID.create(pipeUri);

							recoveryCommunicator.sendGoOnMessage(peer, pipe);
							
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		// Add this info to the local-backup-info
		IRecoveryBackupInformation backupInfo = new BackupInformation();
		backupInfo.setSharedQuery(sharedQueryId);
		backupInfo.setLocalPQL(pql);
		LocalBackupInformationAccess.getStore().add(backupInfo);
		
		if (!foundReceiver) {
			// We don't have a receiver, thus we need a buddy
			recoveryCommunicator.chooseBuddyForQuery(sharedQueryId);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void updateReceiver(PeerID newSender, PipeID pipeId) {
		
		if(!RecoveryCommunicator.getExecutor().isPresent()) {
			
			LOG.error("No executor bound!");
			return;
			
		}
		
		// 1. Get the receiver, which we have to update
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor().get().getExecutionPlan().getQueries();
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

	private static void beBuddy(PeerID sender, ID sharedQueryId, List<String> pqls) {
		LocalBackupInformationAccess.addBuddy(sender, sharedQueryId);
		IRecoveryBackupInformation info = new BackupInformation();
		info.setPeer(sender);
		info.setSharedQuery(sharedQueryId);
		String totalPQL = "";
		for(String pql : pqls) {
			totalPQL += " " + pql;
		}
		info.setPQL(totalPQL);
		LocalBackupInformationAccess.getStore().add(info);
		LOG.debug("I am now the buddy for {}", RecoveryCommunicator.getPeerDictionary().get().getRemotePeerName(sender));
	}

}
