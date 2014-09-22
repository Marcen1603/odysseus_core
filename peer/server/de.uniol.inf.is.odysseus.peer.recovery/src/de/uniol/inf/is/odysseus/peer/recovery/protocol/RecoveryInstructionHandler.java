package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryInstructionHandler {

	public static void handleInstruction(RecoveryInstructionMessage instructionMessage) {
		switch (instructionMessage.getMessageType()) {
		case RecoveryInstructionMessage.HOLD_ON:
			holdOn(instructionMessage.getSharedQueryId());
			break;
		case RecoveryInstructionMessage.ADD_QUERY:
			addQuery(instructionMessage.getPqlQuery());
			break;
		}
	}
	
	private static void holdOn(int queryId) {
		
	}
	
	private static void addQuery(String pql) {
		RecoveryHelper.installAndRunQueryPartFromPql(pql);
	}

}
