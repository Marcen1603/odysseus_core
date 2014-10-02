package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;

public class RecoveryAgreementHandler {

	/**
	 * Seconds we wait until we will just do the recovery
	 */
	public final static int WAIT_SECONDS = 3;

	private final static int WAIT_MS = WAIT_SECONDS * 1000;

	/**
	 * Peer for which we want to do the recovery
	 */
	private static List<PeerID> recoveryPeers;

	public static void handleAgreementMessage(PeerID senderPeer,
			RecoveryAgreementMessage message) {

		// Question: What if we get a message, but we will detect a bit later,
		// that this peer failed -> then we shouldn't do recovery if the other
		// peer has had a higher number

		// See, if we wanted to do recovery for this peer
		if(!recoveryPeers.contains(message.getFailedPeer())) {
			// No, we don't want to do recovery for this failed peer
			// Do nothing, so the other peer can handle this recovery
			return;
		}

		// Calculate, who has the higher "number" from the peerId
		if (calculateNumberForPeer(senderPeer) > calculateNumberForMe()) {

		}

		// If the other one has a higher number -> Delete the failed peer from
		// the list we want to recover
	}

	public static void waitForAndDoRecovery(final PeerID failedPeer,
			final PeerID newPeer) {

		// 1. Save that we want to do the recovery for that failed peer
		recoveryPeers.add(failedPeer);

		// 2. Send to all other peers that we want to do the recovery
		RecoveryCommunicator.getInstance().sendRecoveryAgreementMessage(
				failedPeer);

		// 3. Wait a few seconds until we just do the recovery
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 4. If the time is over and we still think we should do the
				// recovery:
				// Do the recovery
				if (recoveryPeers.contains(failedPeer)) {
					// We still want to do recovery
					RecoveryCommunicator.getInstance().installQueriesOnNewPeer(
							failedPeer, newPeer);
				}
			}
		}, WAIT_MS);

	}

	private static int calculateNumberForPeer(PeerID peer) {
		byte[] idBytes = peer.toString().getBytes();
		int number = 0;
		for (int i = 0; i < idBytes.length; i++) {
			number += idBytes[i];
		}
		return number;
	}

	private static int calculateNumberForMe() {
		return calculateNumberForPeer(RecoveryCommunicator
				.getP2pNetworkManager().getLocalPeerID());
	}

}
