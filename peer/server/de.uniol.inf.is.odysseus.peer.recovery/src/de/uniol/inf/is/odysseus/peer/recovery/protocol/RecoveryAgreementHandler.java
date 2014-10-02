package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;

public class RecoveryAgreementHandler {
	
	
	public static void startWaitForDoingRecovery(PeerID failedPeer) {
		
	}

	public static void handleAgreementMessage(PeerID senderPeer,
			RecoveryAgreementMessage message) {
		// Calculate, who has the higher "number" from the peerId
		byte[] idBytes = senderPeer.toString().getBytes();
		@SuppressWarnings("unused")
		int number = 0;
		for (int i = 0; i < idBytes.length; i++) {
			number += idBytes[i];
		}
	}

}
