package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryInstructionHandler {

	public static void handleInstruction(
			RecoveryInstructionMessage instructionMessage) {
		switch (instructionMessage.getMessageType()) {
		case RecoveryInstructionMessage.HOLD_ON:
			holdOn(instructionMessage.getSharedQueryId());
			break;
		case RecoveryInstructionMessage.ADD_QUERY:
			addQuery(instructionMessage.getPqlQuery());
			break;
		case RecoveryInstructionMessage.NEW_RECEIVER:
			try {
				newReceiver(instructionMessage.getNewReceiver(),
						instructionMessage.getSharedQueryId());
			} catch (DataTransmissionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private static void holdOn(ID queryId) {
		// Here we want to store the tuples
//		 RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>> store =
//		 new RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>>();

		
	}

	private static void addQuery(String pql) {
		RecoveryHelper.installAndRunQueryPartFromPql(pql);
	}

	/**
	 * The peer which receives this message should send the results for the
	 * given queryId to another receiving peer
	 * 
	 * @param newReceiver
	 * @param queryId
	 * @throws DataTransmissionException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void newReceiver(PeerID newReceiver, ID sharedQueryId)
			throws DataTransmissionException {
		// Get my own (old) sender for this sharedQueryId
		PeerID myPeerId = RecoveryCommunicator.getP2pNetworkManager()
				.getLocalPeerID();
		String pipeId = "";
		List<JxtaInformation> jxtaInfo = LocalBackupInformationAccess
				.getJxtaInfoForPeer(myPeerId);
		for (JxtaInformation info : jxtaInfo) {
			if (info.getSharedQueryID().equals(sharedQueryId)
					&& info.getKey().equals(
							RecoveryCommunicator.JXTA_KEY_SENDER_PIPE_ID)) {
				pipeId = info.getValue();
			}
		}

		// New JxtaSender which should send to the new peer
		JxtaSenderAO originalSenderAO = (JxtaSenderAO) RecoveryHelper.getLogicalJxtaOperator(true, pipeId);
		JxtaSenderAO jxtaSenderAO = (JxtaSenderAO) originalSenderAO.clone();
		jxtaSenderAO.setPeerID(newReceiver.toString());
		jxtaSenderAO.setPipeID(pipeId);

		JxtaSenderPO jxtaSender = null;
		jxtaSender = new JxtaSenderPO(jxtaSenderAO);

		// Now do the subscriptions
		JxtaSenderPO originalSender = (JxtaSenderPO) RecoveryHelper
				.getPhysicalJxtaOperator(true, pipeId);
		jxtaSender.setOutputSchema(originalSender.getOutputSchema());
		PhysicalSubscription subscription = originalSender
				.getSubscribedToSource(0);

		if (subscription.getTarget() instanceof AbstractPipe) {
			((AbstractPipe) subscription.getTarget())
					.subscribeSink(jxtaSender, 0,
							subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
			
			jxtaSender.subscribeToSource(subscription.getTarget(),
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		} else if (subscription.getTarget() instanceof AbstractSource) {
			((AbstractSource) subscription.getTarget())
					.subscribeSink(jxtaSender, 0,
							subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
			
			jxtaSender.subscribeToSource(subscription.getTarget(),
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}

	}

}
