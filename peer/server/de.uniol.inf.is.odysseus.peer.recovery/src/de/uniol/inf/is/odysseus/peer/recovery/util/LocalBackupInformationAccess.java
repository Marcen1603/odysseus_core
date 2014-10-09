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
import com.google.common.collect.Maps;

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
	 * Calls {@link IRecoveryBackupInformationStore#addSharedQuery(ID, Map)} of
	 * the local backup information store.
	 * 
	 * @param sharedQueryId
	 *            The shared query id of the distributed query.
	 * @param pqlStatementsMap
	 *            A mapping of query parts (their pql statements) to the
	 *            allocated peers.
	 */
	public static void storeLocal(ID sharedQueryId,
			Map<PeerID, Collection<String>> pqlStatementsMap) {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return;

		}

		boolean success = cInfoStore.get().addSharedQuery(sharedQueryId,
				pqlStatementsMap);
		if (!success) {

			LOG.error(
					"Backup information for shared query id '{}' were already stored!",
					sharedQueryId);
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
	 * Returns the stored PQL-Statements for a given PeerID and sharedQueryID.
	 * 
	 * @param sharedQueryId
	 *            shared queryId of the PQL you want to have
	 * @param peerId
	 *            PeerID of the peer where the PQL is installed
	 * @return
	 */
	public static ImmutableCollection<String> getStoredPQLStatements(
			ID sharedQueryId, PeerID peerId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get().getStoredPQLStatements(sharedQueryId, peerId);

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
			ImmutableCollection<String> pql = getStoredPQLStatements(id, peerId);
			List<String> pqlForQuery = new ArrayList<String>();
			pqlForQuery.addAll(pql);
			SharedQuery query = new SharedQuery(id, pqlForQuery);
			sharedQueries.add(query);
		}

		return sharedQueries;
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
		return cInfoStore.get().getStoredPeers(sharedQueryId);
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
			for (PeerID peer : cInfoStore.get().getStoredPeers(queryId)) {
				if (peer.equals(peerId)) {
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

	/**
	 * All backup information about a distributed query stored in the local
	 * backup information store.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return A mapping of pql statement to peers, where the statements are
	 *         executed.
	 */
	public static Map<PeerID, Collection<String>> getBackupInformation(
			ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId,
				"The ID of the distributed query must be not null!");

		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		Map<PeerID, Collection<String>> backupInfo = Maps.newHashMap();
		for (PeerID peer : cInfoStore.get().getStoredPeers(sharedQueryId)) {

			backupInfo.put(peer,
					cInfoStore.get()
							.getStoredPQLStatements(sharedQueryId, peer));

		}

		return backupInfo;

	}

	/**
	 * Removes given backup information about a distributed query stored in the
	 * local backup information store.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param backupInformation
	 *            A mapping of pql statements (to remove) to peers, where the
	 *            statements are executed.
	 */
	public static void removeLocal(ID sharedQueryId,
			Map<PeerID, Collection<String>> backupInformation) {

		Preconditions.checkNotNull(sharedQueryId,
				"The ID of the distributed query must be not null!");

		Preconditions.checkNotNull(backupInformation,
				"The backup information must be not null!");

		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return;
		}

		for (PeerID peer : backupInformation.keySet()) {

			for (String pqlStatement : backupInformation.get(peer)) {

				cInfoStore.get().removePQLStatement(sharedQueryId, peer,
						pqlStatement);

			}

		}

	}

}