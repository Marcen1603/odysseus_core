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
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
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
	private static final Logger LOG = LoggerFactory.getLogger(LocalBackupInformationAccess.class);

	/**
	 * The bound backup information store, if there is one bound.
	 */
	private static Optional<IRecoveryBackupInformationStore> cInfoStore = Optional.absent();

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
		LOG.debug("Bound {} as a backup information store for recovery.", store.getClass().getSimpleName());

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
			LOG.debug("Unbound {} as a backup information store for recovery.", store.getClass().getSimpleName());

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
	 * Gets all query parts of a given distributed query for which backup information are stored.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return The PQL codes of all query parts of the given distributed query for which backup information are stored.
	 */
	public static ImmutableSet<String> getStoredPQLStatements(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<String> pqls = Sets.newHashSet();

		for (IRecoveryBackupInformation info : cInfoStore.get().get(sharedQueryId)) {
			if (!Strings.isNullOrEmpty(info.getPQL()))
				pqls.add(info.getPQL());

		}

		return ImmutableSet.copyOf(pqls);

	}

	/**
	 * Gets all distributed queries for which backup information are stored and which have a given peer allocated to a
	 * query part of them.
	 * 
	 * @param peerId
	 *            The ID of the peer. <br />
	 *            Must be not null!
	 * @return The helper class instances, each representing a distributed query, of all distributed queries for which
	 *         backup information are stored and which have the given peer allocated to a query part of them.
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
	 * Gets all query parts of a given distributed query for which backup information are stored and which are allocated
	 * to a given peer.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param peerId
	 *            The ID of the peer. <br />
	 *            Must be not null!
	 * @return The PQL codes of all query parts of the given distributed query for which backup information are stored
	 *         and which are allocated to the given peer.
	 */
	public static ImmutableSet<String> getStoredPQLStatements(ID sharedQueryId, PeerID peerId) {

		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(peerId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<String> pqlStatements = getStoredPQLStatements(sharedQueryId);
		Set<String> out = Sets.newHashSet();

		for (String pql : pqlStatements) {

			Optional<IRecoveryBackupInformation> info = cInfoStore.get().get(pql);
			Preconditions.checkArgument(info.isPresent());

			if (info.get().getAboutPeer() != null && info.get().getAboutPeer().equals(peerId)) {

				out.add(pql);
			}

		}

		return ImmutableSet.copyOf(out);

	}

	/**
	 * Removes the information about that given sharedQueryId which is about the given peerId
	 * 
	 * @param sharedQueryId
	 *            The sharedQueryId of the info
	 * @param aboutPeerId
	 *            The peer the information is about
	 */
	public static void removeInformation(ID sharedQueryId, PeerID aboutPeecrId) {
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(aboutPeecrId);

		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return;
		}
		cInfoStore.get().remove(sharedQueryId, aboutPeecrId);
	}

	/**
	 * Gets all distributed queries for which backup information are stored.
	 * 
	 * @return The ids of all distributed queries for which backup information are stored.
	 */
	public static ImmutableSet<ID> getStoredIDs() {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		return cInfoStore.get().getAll().keySet();

	}

	/**
	 * Gets all peers, which are allocated to query parts of a given distributed query and for which backup information
	 * are stored.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return The ids of all peers, which are allocated to query parts of the given distributed query and for which
	 *         backup information are stored.
	 */
	public static ImmutableSet<PeerID> getStoredPeersForSharedQueryId(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<PeerID> peers = Sets.newHashSet();
		for (IRecoveryBackupInformation info : cInfoStore.get().get(sharedQueryId)) {

			if (info.getAboutPeer() != null && !peers.contains(info.getAboutPeer())) {

				peers.add(info.getAboutPeer());

			}

		}

		return ImmutableSet.copyOf(peers);

	}

	/**
	 * Gets all distributed queries, which contain query parts allocated to a given peer and for which backup
	 * information are stored.
	 * 
	 * @param peerId
	 *            The id of the peer. <br />
	 *            Must be not null!
	 * @return The ids of all distributed queries, which contain query parts allocated to the given peer and for which
	 *         backup information are stored.
	 */
	public static ImmutableSet<ID> getStoredSharedQueryIdsForPeer(PeerID peerId) {

		Preconditions.checkNotNull(peerId);

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return null;

		}

		Set<ID> sharedQueryIds = Sets.newHashSet();

		for (ID queryId : cInfoStore.get().getAll().keySet()) {
			for (IRecoveryBackupInformation info : cInfoStore.get().get(queryId)) {
				if (info.getAboutPeer() != null && info.getAboutPeer().equals(peerId)) {
					// This is what we search: For this peer we have a
					// sharedQueryId
					sharedQueryIds.add(queryId);
					break;
				}
			}
		}

		return ImmutableSet.copyOf(sharedQueryIds);
	}

	public static IRecoveryBackupInformation updateLocalPQL(ID sharedQueryId, String oldPQL, String newPQL) {
		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		for (IRecoveryBackupInformation info : cInfoStore.get().get(sharedQueryId)) {
			if (info.getLocalPQL() != null) {
				if (info.getLocalPQL().equals(oldPQL)) {
					info.setLocalPQL(newPQL);
					return info;
				}
			}
		}
		return null;
	}

	/**
	 * Updates the PQL String for a given peer for a given shared query id
	 * 
	 * @param sharedQueryId
	 *            The shared query id the PQL belongs to
	 * @param aboutPeer
	 *            The peer the PQL text is about
	 * @param aboutPeerPQL
	 *            The new PQL text which replaces the old
	 * @param locationPeer
	 *            The peer on which this info is stored
	 * @param localPQL
	 *            The new PQL String
	 */
	public static void updateInfo(ID sharedQueryId, PeerID aboutPeer, String aboutPeerPQL, PeerID locationPeer,
			String localPQL) {
		for (IRecoveryBackupInformation info : cInfoStore.get().get(sharedQueryId)) {
			if (aboutPeer != null && info.getAboutPeer().equals(aboutPeer)) {
				info.setPQL(aboutPeerPQL);
			}
			if (locationPeer != null && info.getLocationPeer().equals(locationPeer)) {
				info.setLocalPQL(localPQL);
			}
		}
	}

	/**
	 * Gets all local query parts of a given distributed query for which backup information are stored.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @return The local PQL codes of all query parts of the given distributed query for which backup information are
	 *         stored.
	 */
	public static ImmutableCollection<String> getLocalPQL(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId);

		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		Set<String> pqls = Sets.newHashSet();

		for (IRecoveryBackupInformation info : cInfoStore.get().get(sharedQueryId)) {
			if (info.getLocalPQL() != null
					&& info.getLocationPeer()
							.equals(RecoveryCommunicator.getP2PNetworkManager().get().getLocalPeerID()))
				pqls.add(info.getLocalPQL());
		}

		return ImmutableSet.copyOf(pqls);

	}

	/**
	 * Adds a peer to the list of peers for which we are a buddy.
	 * 
	 * @param peerId
	 *            The PeerID from the peer which wants that we are the buddy
	 * @param sharedQueryId
	 *            The id of the shared query which we are responsible for if the given peer fails
	 */
	public static void addBuddy(PeerID peerId, ID sharedQueryId) {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return;
		}

		cInfoStore.get().addBuddy(peerId, sharedQueryId);
	}

	/**
	 * Gives you the map of peers you are the buddy for and the id's of the shared queries you have to handle if the
	 * peer fails
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
	 * Adds a peer to the list of my buddies (the peers which are responsible if I fail)
	 * 
	 * @param peerId
	 *            The PeerID from the peer which is my buddy
	 * @param sharedQueryId
	 *            The id of the shared query for which this peer is responsible for if I fail
	 */
	public static void addMyBuddy(PeerID peerId, ID sharedQueryId) {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return;
		}

		cInfoStore.get().addMyBuddy(peerId, sharedQueryId);
	}

	/**
	 * Gives you the map of peers that are my buddies. These peers would be responsible if I fail.
	 * 
	 * @return The Map of peers which are my buddies
	 */
	public static Map<PeerID, List<ID>> getMyBuddyList() {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		return cInfoStore.get().getMyBuddyList();
	}

	/**
	 * Removes a peer from the list of my buddies, e.g., if that other peer fails and can no longer be my buddy
	 * 
	 * @param peerId
	 *            The PeerID from the peer which was my buddy
	 * @return The list with the shared query-ids, if it was a buddy for me, null, if not
	 */
	public static List<ID> removeMyBuddy(PeerID peerId) {
		if (!cInfoStore.isPresent()) {
			LOG.error("No backup information store for recovery bound!");
			return null;
		}

		return cInfoStore.get().removeMyBuddy(peerId);
	}

}