package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
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
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.SharedQuery;

/**
 * A helper class to get access to the local backup information store.
 * 
 * @see IRecoveryBackupInformationStore
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

		Preconditions.checkNotNull(store);
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

		Preconditions.checkNotNull(store);
		if (cInfoStore.isPresent() && cInfoStore.get().equals(store)) {

			cInfoStore = Optional.absent();
			LOG.debug("Unbound {} as a backup information store for recovery.",
					store.getClass().getSimpleName());

		}

	}

	/**
	 * Gets the local backup information store.
	 * 
	 * @return The store instance bound by OSGI-DS.
	 */
	public static IRecoveryBackupInformationStore getStore() {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get();

	}

	/**
	 * Gets all query parts of a given distributed query for which backup
	 * information are stored.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return The PQL codes of all query parts of the given distributed query
	 *         for which backup information are stored.
	 */
	public static ImmutableSet<String> getStoredPQLStatements(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<String> pqls = Sets.newHashSet();

		for (IRecoveryBackupInformation info : cInfoStore.get().get(
				sharedQueryId)) {

			pqls.add(info.getPQL());

		}

		return ImmutableSet.copyOf(pqls);

	}

	/**
	 * Gets all distributed queries for which backup information are stored and
	 * which have a given peer allocated to a query part of them.
	 * 
	 * @param peerId
	 *            The ID of the peer. <br />
	 *            Must be not null!
	 * @return The helper class instances, each representing a distributed
	 *         query, of all distributed queries for which backup information
	 *         are stored and which have the given peer allocated to a query
	 *         part of them.
	 */
	public static ImmutableSet<SharedQuery> getStoredPQLStatements(PeerID peerId) {

		Preconditions.checkNotNull(peerId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<SharedQuery> sharedQueries = Sets.newHashSet();

		Set<ID> storedIds = getStoredSharedQueryIdsForPeer(peerId);

		for (ID id : storedIds) {

			ImmutableCollection<String> pql = getStoredPQLStatements(id);
			List<String> pqlForQuery = new ArrayList<String>();
			pqlForQuery.addAll(pql);
			SharedQuery query = new SharedQuery(id, pqlForQuery);
			sharedQueries.add(query);

		}

		return ImmutableSet.copyOf(sharedQueries);
	}

	/**
	 * Gets all query parts of a given distributed query for which backup
	 * information are stored and which are allocated to a given peer.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param peerId
	 *            The ID of the peer. <br />
	 *            Must be not null!
	 * @return The PQL codes of all query parts of the given distributed query
	 *         for which backup information are stored and which are allocated
	 *         to the given peer.
	 */
	public static ImmutableSet<String> getStoredPQLStatements(ID sharedQueryId,
			PeerID peerId) {

		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(peerId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<String> pqlStatements = getStoredPQLStatements(sharedQueryId);
		Set<String> out = Sets.newHashSet();

		for (String pql : pqlStatements) {

			Optional<IRecoveryBackupInformation> info = cInfoStore.get().get(
					pql);
			Preconditions.checkArgument(info.isPresent());

			if (info.get().getPeer().equals(peerId)) {

				out.add(pql);
			}

		}

		return ImmutableSet.copyOf(out);

	}

	/**
	 * Gets all distributed queries for which backup information are stored.
	 * 
	 * @return The ids of all distributed queries for which backup information
	 *         are stored.
	 */
	public static ImmutableSet<ID> getStoredIDs() {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get().getAll().keySet();

	}

	/**
	 * Gets all peers, which are allocated to query parts of a given distributed
	 * query and for which backup information are stored.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return The ids of all peers, which are allocated to query parts of the
	 *         given distributed query and for which backup information are
	 *         stored.
	 */
	public static ImmutableSet<PeerID> getStoredPeersForSharedQueryId(
			ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<PeerID> peers = Sets.newHashSet();
		for (IRecoveryBackupInformation info : cInfoStore.get().get(
				sharedQueryId)) {

			if (!peers.contains(info.getPeer())) {

				peers.add(info.getPeer());

			}

		}

		return ImmutableSet.copyOf(peers);

	}

	/**
	 * Gets all distributed queries, which contain query parts allocated to a
	 * given peer and for which backup information are stored.
	 * 
	 * @param peerId
	 *            The id of the peer. <br />
	 *            Must be not null!
	 * @return The ids of all distributed queries, which contain query parts
	 *         allocated to the given peer and for which backup information are
	 *         stored.
	 */
	public static ImmutableSet<ID> getStoredSharedQueryIdsForPeer(PeerID peerId) {

		Preconditions.checkNotNull(peerId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<ID> sharedQueryIds = Sets.newHashSet();

		for (ID queryId : cInfoStore.get().getAll().keySet()) {
			for (IRecoveryBackupInformation info : cInfoStore.get()
					.get(queryId)) {
				if (info.getPeer().equals(peerId)) {
					// This is what we search: For this peer we have a
					// sharedQueryId
					sharedQueryIds.add(queryId);
					break;
				}
			}
		}

		return ImmutableSet.copyOf(sharedQueryIds);
	}

	/*
	 * TODO Jxta information. Not clear atm. if they are needed.
	 */

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

	/*
	 * TODO Buddy information. Not clear atm. if they are needed.
	 */

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