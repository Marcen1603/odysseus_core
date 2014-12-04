package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

/**
 * The backup information store for recovery. <br />
 * It stores multiple backup information.
 * 
 * A backup information is for query parts, which are executed on another peer. <br />
 * An information consists of the following:<br />
 * - id of the distributed query <br />
 * - the PQL code of the query part <br />
 * - the id of the peer, where the query part is executed <br />
 * - information about subsequent parts (relative to the query part): PQL code and peer, where it is executed <br />
 * - the PQL code of the local part for which the backup information are.
 * 
 * The stored information are about query parts, which are subsequent to query parts executed on the local peer.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryBackupInformationStore {

	/**
	 * Adds a new backup information.
	 * 
	 * @param info
	 *            The information to add. <br />
	 *            Must be not null.
	 */
	public void add(IRecoveryBackupInformation info);

	/**
	 * Gets all stored information.
	 * 
	 * @return A mapping of information to distributed query ids (for which the information are part of).
	 */
	public ImmutableMap<ID, Collection<IRecoveryBackupInformation>> getAll();

	/**
	 * Gets all stored information about a given distributed query.
	 * 
	 * @param sharedQuery
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 * @return All stored information for the distributed query.
	 */
	public ImmutableCollection<IRecoveryBackupInformation> get(ID sharedQuery);

	/**
	 * Gets information about a given query part.
	 * 
	 * @param pql
	 *            The PQL code of the query part. <br />
	 *            Must be not null.
	 * @return The stored information or {@link Optional#absent()}, if there is no information stored.
	 */
	public Optional<IRecoveryBackupInformation> get(String pql);

	/**
	 * Removes all stored information about a given distributed query.
	 * 
	 * @param sharedQuery
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 */
	public void remove(ID sharedQuery);

	/**
	 * Removes a stored information.
	 * 
	 * @param info
	 *            The information to remove. <br />
	 *            Must be not null.
	 */
	public void remove(IRecoveryBackupInformation info);

	/**
	 * Removes all stored information about a given query part.
	 * 
	 * @param pql
	 *            The PQL code of the query part. <br />
	 *            Must be not null.
	 */
	public void remove(String pql);

	/**
	 * Removes the information about that given sharedQueryId which is about the given peerId
	 * 
	 * @param sharedQueryId
	 *            The sharedQueryId of the info
	 * @param aboutPeerId
	 *            The peer the information is about
	 */
	void remove(ID sharedQueryId, PeerID aboutPeerId);

	/*
	 * Buddy-information (the peers for which we are the buddy)
	 */

	/**
	 * Adds a peer to the list of peers for which we are a buddy.
	 * 
	 * @param peerId
	 *            The PeerID from the peer which wants that we are the buddy
	 * @param sharedQueryId
	 *            The id of the shared query which we are responsible for if the given peer fails
	 */
	public void addBuddy(PeerID peerId, ID sharedQueryId);

	/**
	 * Gives you the map of peers you are the buddy for and the id's of the shared queries you have to handle if the
	 * peer fails
	 * 
	 * @return The Map of peers you are the buddy for
	 */
	public Map<PeerID, List<ID>> getBuddyList();

	/*
	 * My buddies (the peers which are my buddies)
	 */

	/**
	 * Adds a peer to the list of peers which are my buddies (so they would do the recovery if I would fail)
	 * 
	 * @param peerId
	 *            The PeerID from the peer which is my buddy
	 * @param sharedQueryId
	 *            The id of the shared query for which this peer is responsible for if I fail
	 */
	public void addMyBuddy(PeerID peerId, ID sharedQueryId);

	/**
	 * Removes a peer from the list of my buddies, e.g., if that other peer fails and can no longer be my buddy
	 * 
	 * @param peerId
	 *            The PeerID from the peer which was my buddy
	 * @return The list of sharedQueryIds the other peer was responsible for
	 */
	public List<ID> removeMyBuddy(PeerID peerId);

	/**
	 * Gives you the map of peers that are my buddies. These peers would be responsible if I fail.
	 * 
	 * @return The Map of peers which are my buddies
	 */
	public Map<PeerID, List<ID>> getMyBuddyList();

	/**
	 * Gives information, if the store has information about the given shared query id
	 * 
	 * @param sharedQuery
	 *            The id of the shared query you want to know if this store has information about it
	 * @return true, if the store has information, false, if not
	 */
	boolean hasInformationAbout(ID sharedQuery);

	/**
	 * Removes a shared query from the list of peers where we are the buddy for. Searches through all saved peers we are
	 * the buddy for and deletes the shared query id. This is useful, if a shared query is deleted. Attention:
	 * This does not changes anything at the buddies them self.
	 * 
	 * @param sharedQueryID
	 *            The id of the shared query which has to be removed
	 */
	void removeSharedQueryFromBuddyList(ID sharedQueryId);

	/**
	 * Removes a shared query from the list of my buddies. Searches through all saved buddies and deletes the shared
	 * query id. This is useful, if a shared query is deleted. Attention: This does not changes anything at the buddies
	 * them self.
	 * 
	 * @param sharedQueryID
	 *            The id of the shared query which has to be removed
	 */
	void removeSharedQueryFromMyBuddyList(ID sharedQueryID);

}