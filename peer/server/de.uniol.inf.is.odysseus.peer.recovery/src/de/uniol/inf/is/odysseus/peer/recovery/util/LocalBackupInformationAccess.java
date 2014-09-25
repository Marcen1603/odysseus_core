package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
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
		
		for(ID id : storedIds) {
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
	 * @param peerId Peer you want to have the sharedQueryIds from
	 * @return
	 */
	public static List<ID> getStoredSharedQueryIdsForPeer(
			PeerID peerId) {
		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}
		
		List<ID> sharedQueryIds = new ArrayList<ID>();

		for (ID queryId : cInfoStore.get().getStoredSharedQueries()) {
			for (PeerID peer : cInfoStore.get().getStoredPeers(queryId)) {
				if (peer.equals(peerId)) {
					// This is what we search: For this peer we have a sharedQueryId
					sharedQueryIds.add(queryId);
				}
			}
		}

		return sharedQueryIds;
	}

}