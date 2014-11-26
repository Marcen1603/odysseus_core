package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;

public class RecoveryAgreementHandler {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryAgreementHandler.class);

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator, "The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator.getClass().getSimpleName());

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

		Preconditions.checkNotNull(communicator, "The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent() && cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator.getClass().getSimpleName());

		}

	}

	/**
	 * Seconds we wait until we will just do the recovery
	 */
	public final static int WAIT_SECONDS = 3;

	private final static int WAIT_MS = WAIT_SECONDS * 1000;

	/**
	 * Peers and the (maybe multiple) shared queries we want to do the recovery for
	 */
	private static Map<PeerID, List<ID>> recoveryPeers = new HashMap<PeerID, List<ID>>();

	/**
	 * Failed peers and the (maybe multiple) shared queries other peers have detected earlier and want to do the
	 * recovery for
	 */
	private static Map<PeerID, List<ID>> otherRecoveryPeers = new HashMap<PeerID, List<ID>>();

	public static void handleAgreementMessage(PeerID senderPeer, RecoveryAgreementMessage message) {

		// Question to think about later: What if we get a message, but we will
		// detect a bit later,
		// that this peer failed -> then we shouldn't do recovery if the other
		// peer has had a higher number

		// See, if we wanted to do recovery for this peer and this sharedQuery
		if (!recoveryPeers.containsKey(message.getFailedPeer())) {
			// No, we don't want to do recovery for this failed peer
			// Save, that another peer does this recovery -> We don't want to do it again if we recognize the failure
			// later
			saveToOtherRecoveryPeersList(message);
			return;
		} else {
			// See, if we want to do the recovery for this query
			List<ID> queries = recoveryPeers.get(message.getFailedPeer());
			if (!queries.contains(message.getSharedQueryId())) {
				// We don't want to do recovery for this query
				return;
			}
		}

		// Calculate, who has the higher "number" from the peerId
		if (!thisPeerHasHigherNumber(senderPeer)) {
			// The other one has a higher number. We won't do recovery for this
			// failed peer for this query

			// Remove that we want to do recovery for this query on this peer
			removeBackupQueueEntry(message.getFailedPeer(), message.getSharedQueryId());

			return;
		}

		// Okay, we still want to do the recovery, so do nothing (the failed
		// peer will remain in the list and we will do the recovery after the
		// time is over)
	}

	/**
	 * Saves, that we want to do recovery for the given query on the given peer, tells the other peers that we want to
	 * do this, waits and finally (if no other one wants to do the recovery), does the recovery
	 * 
	 * @param failedPeer
	 * @param sharedQueryId
	 * @param newPeer
	 *            The peer where we want to install the parts of the query from the failed peer
	 * @param recoveryStateIdentifier
	 */
	public static void waitForAndDoRecovery(final PeerID failedPeer, final ID sharedQueryId, final PeerID newPeer,
			final ILogicalQueryPart queryPart, final UUID recoveryStateIdentifier) {

		if (!cCommunicator.isPresent()) {
			LOG.error("No recovery communicator bound!");
			return;
		}

		// 1. Check, if another peer detected the failure earlier and will do the recovery
		if (otherPeerDoesRecovery(failedPeer, sharedQueryId))
			return;

		// 2. Check, if we already want to do recovery for another query for
		// that failed peer
		List<ID> queriesForPeer = new ArrayList<ID>();
		if (recoveryPeers.containsKey(failedPeer)) {
			queriesForPeer = recoveryPeers.get(failedPeer);
		}

		// 3. Save that we want to do the recovery for that failed peer
		queriesForPeer.add(sharedQueryId);
		recoveryPeers.put(failedPeer, queriesForPeer);

		// 4. Send to all other peers that we want to do the recovery
		cCommunicator.get().sendRecoveryAgreementMessage(failedPeer, sharedQueryId, recoveryStateIdentifier);

		// 5. Wait a few seconds until we just do the recovery
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 6. If the time is over and we still think we should do the
				// recovery: Do the recovery
				if (recoveryPeers.containsKey(failedPeer) && recoveryPeers.get(failedPeer).contains(sharedQueryId)) {
					// We still want to do recovery for that peer for that query id

					// get PQL from query part
					String pql = LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart);

					cCommunicator.get().installQueriesOnNewPeer(failedPeer, newPeer, sharedQueryId, pql,
							recoveryStateIdentifier);

					// Now we did this, so remove that we want to do this recovery
					removeBackupQueueEntry(failedPeer, sharedQueryId);
				}
			}
		}, WAIT_MS);

	}

	/**
	 * Checks, if another peer already detected the failed peer earlier. Returns true, if so (this peer shouldn't do
	 * recovery then) and false, if not. Removes the sharedQueryId from the list cause we won't detect a failure twice
	 * and don't want to save unnecessary information. Thus, this method has side-effects: If you call it the second
	 * time with the same parameters and it first returned true, it will return false!
	 * 
	 * @param failedPeer
	 *            The failed peer this peer detected
	 * @param sharedQueryId
	 *            The shared query id this peer now (maybe) wants to do recovery for
	 * @return true, if another peer already detected this and will do the recovery. False, if not (in this case you can
	 *         try to do recovery)
	 */
	private static boolean otherPeerDoesRecovery(PeerID failedPeer, ID sharedQueryId) {
		if (otherRecoveryPeers.containsKey(failedPeer)) {
			List<ID> sharedQueryIds = otherRecoveryPeers.get(failedPeer);
			if (sharedQueryIds.contains(sharedQueryId)) {
				// Another peer already detected this failure - and maybe already begun or finished the recovery. So
				// don't do anything but to delete this entry from the list (cause we won't detect the failure twice and
				// thus we don't need to save it longer than necessary)
				sharedQueryIds.remove(sharedQueryId);
				if (sharedQueryIds.isEmpty()) {
					otherRecoveryPeers.remove(failedPeer);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * Saves, that another peer wants to do the recovery for this shared query. Use this, if this peer doesn't want to
	 * to recovery for this so far.
	 * 
	 * @param message
	 *            The message from the other peer who wants to do recovery
	 */
	private static void saveToOtherRecoveryPeersList(RecoveryAgreementMessage message) {
		if (!otherRecoveryPeers.containsKey(message.getFailedPeer())) {
			List<ID> sharedQueryIds = new ArrayList<ID>();
			sharedQueryIds.add(message.getSharedQueryId());
			otherRecoveryPeers.put(message.getFailedPeer(), sharedQueryIds);
		} else {
			List<ID> sharedQueryIds = otherRecoveryPeers.get(message.getFailedPeer());
			sharedQueryIds.add(message.getSharedQueryId());
			otherRecoveryPeers.put(message.getFailedPeer(), sharedQueryIds);
		}
	}

	/**
	 * To compare two peerIds.
	 * 
	 * @param peer
	 *            Peer to compare with this peer
	 * @return true, if we have the "higher number", false, if the other one has
	 */
	private static boolean thisPeerHasHigherNumber(PeerID peer) {

		if (!RecoveryCommunicator.getP2PNetworkManager().isPresent()) {

			LOG.error("No P2P network manager bound!");
			return false;

		}

		return RecoveryCommunicator.getP2PNetworkManager().get().getLocalPeerID().toString().compareTo(peer.toString()) >= 0;
	}

	/**
	 * Removes an entry that we want (or have to do) this recovery
	 * 
	 * @param failedPeer
	 * @param sharedQueryId
	 */
	private static void removeBackupQueueEntry(PeerID failedPeer, ID sharedQueryId) {
		List<ID> queries = recoveryPeers.get(failedPeer);
		queries.remove(sharedQueryId);

		if (queries.size() <= 0) {
			// If we don't have any queries left we want to recovery for a
			// failed peer, delete the peer
			recoveryPeers.remove(failedPeer);
		}
	}

}
