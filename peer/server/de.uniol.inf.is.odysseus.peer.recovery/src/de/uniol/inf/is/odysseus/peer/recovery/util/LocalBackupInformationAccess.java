package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.SharedQuery;

/**
 * A helper class to backup information about distributed queries within the
 * local {@link IRecoveryBackupInformationStore}.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class LocalBackupInformationAccess {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LocalBackupInformationAccess.class);

	/**
	 * The bound backup information store, if there is one bound.
	 */
	private static Optional<IRecoveryBackupInformationStore> cInfoStore = Optional
			.absent();

	/**
	 * Binds a backup information store. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param store
	 *            The backup information store to bind. <br />
	 *            Must be not null.
	 */
	public static void bindStore(IRecoveryBackupInformationStore store) {

		Preconditions.checkNotNull(store,
				"The backup information store to bind must be not null!");
		cInfoStore = Optional.of(store);
		LOG.debug("Bound {} as a backup information store for recovery.", store
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a backup information store. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param store
	 *            The backup information store to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindStore(IRecoveryBackupInformationStore store) {

		Preconditions.checkNotNull(store,
				"The backup information store to unbind must be not null!");
		if (cInfoStore.isPresent() && cInfoStore.get().equals(store)) {

			cInfoStore = Optional.absent();
			LOG.debug("Unbound {} as a backup information store for recovery.",
					store.getClass().getSimpleName());

		}

	}

	/**
	 * Calls
	 * {@link IRecoveryBackupInformationStore#add(ID, String, PeerID, Map)}.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 * @param peer
	 *            The ID of the peer, where the PQL statement is installed. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            The subsequent parts of the PQL statement as a mapping of the
	 *            PQL statements of the subsequent parts and the IDs of the
	 *            peers, where the subsequent PQL statements are installed. <br />
	 *            Must be not null.
	 */
	public static void store(ID sharedQueryId, String pqlStatement,
			PeerID peer, Map<String, PeerID> subsequentParts) {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return;

		}

		boolean success = cInfoStore.get().add(sharedQueryId, pqlStatement,
				peer, subsequentParts);
		if (!success) {

			LOG.error(
					"Backup information for PQL statement '{}' were already stored!",
					pqlStatement);
			return;

		}

	}

	/**
	 * Stores a key-value pair for a certain shared query on a certain peer.
	 * E.g. pipe-ids, etc.
	 * 
	 * @param peerId
	 *            PeerID from the peer from which the info is (e.g. if you store
	 *            the pipe-id for a jxtaReceiver, this is the peer where the
	 *            jxtaReceiver is installed on)
	 * @param sharedQueryId
	 *            Id of the shared query for which this information is (e.g. if
	 *            you store the pipe-id for a jxtaReceiver, this is the queryId
	 *            from the certain jxtaReceiver)
	 * @param key
	 *            Key for the key-value-pair
	 * @param value
	 *            value for the key-value-pair, e.g. the pipe-id
	 */
	public static void storeLocalJxtaInfo(PeerID peerId, ID sharedQueryId,
			String key, String value) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return;

		}
		cInfoStore.get().addJxtaInfo(peerId, sharedQueryId, key, value);
	}

	/**
	 * All jxta-information that is stored for a given peer
	 * 
	 * @param peerId
	 *            The id of the peer you want to have the information about
	 * @return A list of jxta-information about the given peer
	 */
	public static List<JxtaInformation> getJxtaInfoForPeer(PeerID peerId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get().getJxtaInfoForPeer(peerId);
	}

	/**
	 * 
	 * @return All PeerIDs for which jxta-backup-information is stored
	 */
	public static List<PeerID> getPeersFromJxtaInfoStore() {

		Set<PeerID> peers = cInfoStore.get().getPeersFromJxtaInfoStore();
		ArrayList<PeerID> peerArray = new ArrayList<PeerID>(peers);
		return peerArray;
	}

	/**
	 * Calls {@link IRecoveryBackupInformationStore#getStoredPQLStatements(ID)}
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 */
	public static ImmutableCollection<String> getStoredPQLStatements(
			ID sharedQueryId) {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get().getStoredPQLStatements(sharedQueryId);

	}

	/**
	 * Returns the stored PQL-Statements for a given PeerID.
	 * 
	 * @param peerId
	 * @return
	 */
	public static List<SharedQuery> getStoredPQLStatements(PeerID peerId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		List<SharedQuery> sharedQueries = new ArrayList<SharedQuery>();

		List<ID> storedIds = getStoredSharedQueryIdsForPeer(peerId);

		for (ID id : storedIds) {
			ImmutableCollection<String> pql = getStoredPQLStatements(id);
			List<String> pqlForQuery = new ArrayList<String>();
			pqlForQuery.addAll(pql);
			SharedQuery query = new SharedQuery(id, pqlForQuery);
			sharedQueries.add(query);
		}

		return sharedQueries;
	}

	public static ImmutableCollection<String> getStoredPQLStatements(
			ID sharedQueryId, PeerID failedPeer) {

		Collection<String> results = Lists.newArrayList();
		ImmutableCollection<String> results1 = getStoredPQLStatements(sharedQueryId);
		ImmutableCollection<String> results2 = getStoredPQLStatements(sharedQueryId);

		for (String pqlStatement : results1) {

			if (results2.contains(pqlStatement)) {

				results.add(pqlStatement);

			}

		}

		return ImmutableSet.copyOf(results);

	}

	/**
	 * 
	 * @return All stored sharedQueryIDs in the backup-store of this peer
	 *         (hence, the ids can (and probably will) be from many peers
	 */
	public static ImmutableCollection<ID> getStoredIDs() {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}
		return cInfoStore.get().getStoredSharedQueries();
	}

	/**
	 * 
	 * @param sharedQueryId
	 * @return All stored peerIds which are known to have a part of the query
	 *         with the given sharedQueryId
	 */
	public static ImmutableCollection<PeerID> getStoredPeersForSharedQueryId(
			ID sharedQueryId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Collection<PeerID> peers = Lists.newArrayList();
		for (String pqlStatement : cInfoStore.get().getStoredPQLStatements(
				sharedQueryId)) {

			Optional<PeerID> peer = cInfoStore.get().getStoredPeerID(
					sharedQueryId, pqlStatement);
			if (peer.isPresent() && !peers.contains(peer.get())) {

				peers.add(peer.get());

			}

		}

		return ImmutableSet.copyOf(peers);
	}

	/**
	 * Returns all shared query-ids which have a query part on the given peer
	 * 
	 * @param peerId
	 *            Peer you want to have the sharedQueryIds from
	 * @return
	 */
	public static List<ID> getStoredSharedQueryIdsForPeer(PeerID peerId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		List<ID> sharedQueryIds = new ArrayList<ID>();

		for (ID queryId : cInfoStore.get().getStoredSharedQueries()) {
			for (String pqlStatement : cInfoStore.get().getStoredPQLStatements(
					queryId)) {
				Optional<PeerID> peer = cInfoStore.get().getStoredPeerID(
						queryId, pqlStatement);
				if (peer.isPresent() && peer.get().equals(peerId)) {
					// This is what we search: For this peer we have a
					// sharedQueryId
					sharedQueryIds.add(queryId);
				}
			}
		}

		return sharedQueryIds;
	}

	/**
	 * Adds a peer to the list of peers for which we are a buddy.
	 * 
	 * @param peerId
	 *            The PeerID from the peer which wants that we are the buddy
	 * @param sharedQueryId
	 *            The id of the shared query which we are responsible for if the
	 *            given peer fails
	 */
	public static void addBuddy(PeerID peerId, ID sharedQueryId) {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return;
		}

		cInfoStore.get().addBuddy(peerId, sharedQueryId);
	}

	/**
	 * Gives you the map of peers you are the buddy for and the id's of the
	 * shared queries you have to handle if the peer fails
	 * 
	 * @return The Map of peers you are the buddy for
	 */
	public static Map<PeerID, List<ID>> getBuddyList() {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		return cInfoStore.get().getBuddyList();
	}

}