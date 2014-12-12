package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;

/**
 * Helper class for recovery agreements.
 * 
 * @author Michael Brand & Tobias Brandt
 *
 */
public class AgreementHelper {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AgreementHelper.class);

	/**
	 * Seconds we wait until we will just do the recovery
	 */
	public final static int WAIT_SECONDS = 3;

	private final static int WAIT_MS = WAIT_SECONDS * 1000;

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);

		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {

			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public void bindCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		cCommunicator = Optional.of(serv);
		LOG.debug("Bound {} as a recovery communicator.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);

		if (cCommunicator.isPresent() && cCommunicator.get() == serv) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Peers and the (maybe multiple) shared queries we want to do the recovery for
	 */
	private static Map<PeerID, List<Integer>> cRecoveryPeers = new HashMap<PeerID, List<Integer>>();

	/**
	 * Failed peers and the (maybe multiple) shared queries other peers have detected earlier and want to do the
	 * recovery for
	 */
	private static Map<PeerID, List<Integer>> cOtherRecoveryPeers = new HashMap<PeerID, List<Integer>>();

	/**
	 * Checks, if this peer wants to recover parts of a given shared query of a given failed peer.
	 * 
	 * @param failedPeer
	 *            The given failed peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The given shared query. <br />
	 *            Must be not null.
	 * @param sender
	 *            The peer, which has send the request. <br />
	 *            Must be not null.
	 * @return True, if this peer is able and willing to recover the given failed peer; false, else.
	 */
	public static boolean decideToRecover(PeerID failedPeer, int localQuery, PeerID sender) {
		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(sender);

		// Question to think about later: What if we get a message, but we will
		// detect a bit later,
		// that this peer failed -> then we shouldn't do recovery if the other
		// peer has had a higher number

		// See, if we wanted to do recovery for this peer and this sharedQuery
		if (!cRecoveryPeers.containsKey(failedPeer)) {
			// No, we don't want to do recovery for this failed peer
			// Save, that another peer does this recovery -> We don't want to do
			// it again if we recognize the failure
			// later
			saveToOtherRecoveryPeersList(failedPeer, localQuery);
			return false;
		} else {
			// See, if we want to do the recovery for this query
			List<Integer> queries = cRecoveryPeers.get(failedPeer);
			if (!queries.contains(localQuery)) {
				// We don't want to do recovery for this query
				return false;
			}
		}

		// Calculate, who has the higher "number" from the peerId
		if (!thisPeerHasHigherNumber(sender)) {
			// The other one has a higher number. We won't do recovery for this
			// failed peer for this query

			// Remove that we want to do recovery for this query on this peer
			removeBackupQueueEntry(failedPeer, localQuery);

			return false;
		}

		// Okay, we still want to do the recovery, so do nothing (the failed
		// peer will remain in the list and we will do the recovery after the
		// time is over)
		return true;

	}

	/**
	 * Saves, that another peer wants to do the recovery for this shared query. Use this, if this peer doesn't want to
	 * to recovery for this so far.
	 * 
	 * @param failedPeer
	 *            The given failed peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The given shared query. <br />
	 *            Must be not null.
	 */
	private static void saveToOtherRecoveryPeersList(PeerID failedPeer, int localQueryId) {
		Preconditions.checkNotNull(failedPeer);

		List<Integer> localQueryIds = new ArrayList<Integer>();
		if (!cOtherRecoveryPeers.containsKey(failedPeer)) {
			localQueryIds.add(localQueryId);
		} else {
			localQueryIds = cOtherRecoveryPeers.get(failedPeer);
			localQueryIds.add(localQueryId);
		}
		cOtherRecoveryPeers.put(failedPeer, localQueryIds);
	}

	/**
	 * To compare two peerIds.
	 * 
	 * @param peer
	 *            Peer to compare with this peer
	 * @return true, if we have the "higher number", false, if the other one has
	 */
	private static boolean thisPeerHasHigherNumber(PeerID peer) {
		Preconditions.checkNotNull(peer);

		if (!cP2PNetworkManager.isPresent()) {

			LOG.error("No P2P network manager bound!");
			return false;

		}

		return cP2PNetworkManager.get().getLocalPeerID().toString().compareTo(peer.toString()) >= 0;
	}

	/**
	 * Removes an entry that we want (or have to do) this recovery
	 */
	private static void removeBackupQueueEntry(PeerID failedPeer, Integer localQueryId) {
		Preconditions.checkNotNull(failedPeer);

		List<Integer> queries = cRecoveryPeers.get(failedPeer);
		queries.remove(localQueryId);

		if (queries.size() <= 0) {
			// If we don't have any queries left we want to recovery for a
			// failed peer, delete the peer
			cRecoveryPeers.remove(failedPeer);
		}
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
	public static void waitForAndDoRecovery(final PeerID failedPeer, final int localQueryId, final PeerID newPeer,
			final ILogicalQueryPart queryPart, final UUID recoveryStateIdentifier, final UUID subprocessID) {

		if (!cCommunicator.isPresent()) {
			LOG.error("No recovery communicator bound!");
			return;
		}

		// 1. Check, if another peer detected the failure earlier and will do the recovery
		if (otherPeerDoesRecovery(failedPeer, localQueryId)) {
			LOG.debug("Another peer was faster - he can do recovery, I won't do it.");
			return;
		}

		// 2. Check, if we already want to do recovery for another query for
		// that failed peer
		List<Integer> queriesForPeer = new ArrayList<Integer>();
		if (cRecoveryPeers.containsKey(failedPeer)) {
			queriesForPeer = cRecoveryPeers.get(failedPeer);
		}

		// 3. Save that we want to do the recovery for that failed peer
		queriesForPeer.add(localQueryId);
		cRecoveryPeers.put(failedPeer, queriesForPeer);

		// 4. Send to all other peers that we want to do the recovery
		cCommunicator.get().sendRecoveryAgreementMessage(failedPeer, localQueryId);

		// 5. Wait a few seconds until we just do the recovery
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 6. If the time is over and we still think we should do the
				// recovery: Do the recovery
				if (cRecoveryPeers.containsKey(failedPeer) && cRecoveryPeers.get(failedPeer).contains(localQueryId)) {
					// We still want to do recovery for that peer for that query id
					LOG.debug("Now I start with the revoery for {}", failedPeer);

					// get PQL from query part
					String pql = LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart);

					cCommunicator.get().installQueriesOnNewPeer(failedPeer, newPeer, localQueryId, pql,
							recoveryStateIdentifier, subprocessID);

					// Now we did this, so remove that we want to do this recovery
					removeBackupQueueEntry(failedPeer, localQueryId);
				} else {
					LOG.debug("Another peer won the peerId-battle for this peer, I won't do recovery for {}",
							failedPeer);
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
	private static boolean otherPeerDoesRecovery(PeerID failedPeer, int localQueryId) {
		Preconditions.checkNotNull(failedPeer);

		if (cOtherRecoveryPeers.containsKey(failedPeer)) {
			List<Integer> localQueryIds = cOtherRecoveryPeers.get(failedPeer);
			if (localQueryIds.contains(localQueryId)) {
				// Another peer already detected this failure - and maybe already begun or finished the recovery. So
				// don't do anything but to delete this entry from the list (cause we won't detect the failure twice and
				// thus we don't need to save it longer than necessary)
				localQueryIds.remove(new Integer(localQueryId));
				if (localQueryIds.isEmpty()) {
					cOtherRecoveryPeers.remove(failedPeer);
				}
				return true;
			}
		}

		return false;
	}

}