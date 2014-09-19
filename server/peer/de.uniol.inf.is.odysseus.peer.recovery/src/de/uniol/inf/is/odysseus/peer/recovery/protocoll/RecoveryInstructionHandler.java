package de.uniol.inf.is.odysseus.peer.recovery.protocoll;

import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;

public class RecoveryInstructionHandler {

	public void handleInstruction(RecoveryInstructionMessage instructionMessage) {
		switch (instructionMessage.getMessageType()) {
		case RecoveryInstructionMessage.HOLD_ON:
			holdOn(instructionMessage.getSharedQueryId());
			break;
		}
	}
	
	private void holdOn(int queryId) {
		
	}

}
