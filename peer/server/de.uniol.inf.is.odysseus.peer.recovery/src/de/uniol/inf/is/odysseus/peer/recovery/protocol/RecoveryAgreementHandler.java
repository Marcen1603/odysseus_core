package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;

public class RecoveryAgreementHandler {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryAgreementHandler.class);

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent()
				&& cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator
					.getClass().getSimpleName());

		}

	}

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

		// Question to think about later: What if we get a message, but we will
		// detect a bit later,
		// that this peer failed -> then we shouldn't do recovery if the other
		// peer has had a higher number

		// See, if we wanted to do recovery for this peer
		if (!recoveryPeers.contains(message.getFailedPeer())) {
			// No, we don't want to do recovery for this failed peer
			// Do nothing, so the other peer can handle this recovery
			return;
		}

		// Calculate, who has the higher "number" from the peerId
		if (calculateNumberForPeer(senderPeer) > calculateNumberForMe()) {
			// The other one has a higher number. We won't do recovery for this
			// failed peer
			recoveryPeers.remove(message.getFailedPeer());
			return;
		}

		// Okay, we still want to do the recovery, so do nothing (the failed
		// peer
		// will remain in the list and we will do the recovery after the time is
		// over)
	}

	public static void waitForAndDoRecovery(final PeerID failedPeer,
			final PeerID newPeer) {

		if (!cCommunicator.isPresent()) {
			LOG.error("No recovery communicator bound!");
			return;
		}

		// 1. Save that we want to do the recovery for that failed peer
		recoveryPeers.add(failedPeer);

		// 2. Send to all other peers that we want to do the recovery
		cCommunicator.get().sendRecoveryAgreementMessage(failedPeer);

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
					cCommunicator.get().installQueriesOnNewPeer(failedPeer,
							newPeer);
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
