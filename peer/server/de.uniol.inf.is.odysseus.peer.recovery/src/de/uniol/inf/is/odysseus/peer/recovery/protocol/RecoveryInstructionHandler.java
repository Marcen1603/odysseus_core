package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
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
			newReceiver(instructionMessage.getNewReceiver(),
					instructionMessage.getSharedQueryId());
			break;
		}
	}

	private static void holdOn(ID queryId) {
		// Here we want to store the tuples
		// RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>> store =
		// new RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>>();

		// Get the query for that id (hope that the sharedQueryID is the queryId
		// in "getQueryById"
		// TODO Test if it's the right id
		// IPhysicalQuery query =
		// RecoveryCommunicator.getExecutor().getExecutionPlan().getQueryById(queryId);

		// store.subscribeToSource(source, sinkInPort, sourceOutPort, schema)
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
	 */
	private static void newReceiver(PeerID newReceiver, ID sharedQueryId) {
//		JxtaSenderAO newSenderAO = new JxtaSenderAO();
//
//		try {
//			JxtaSenderPO<IStreamObject<?>> newSender = new JxtaSenderPO<IStreamObject<?>>(
//					newSenderAO);
//		} catch (DataTransmissionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//ID sharedId = new ID 
//		LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId, newReceiver);
	}

}
