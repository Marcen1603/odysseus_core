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
 * - information about subsequent parts (relative to the query part): PQL code
 * and peer, where it is executed <br />
 * - the PQL code of the local part for which the backup information are.
 * 
 * The stored information are about query parts, which are subsequent to query
 * parts executed on the local peer.
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
	 * @return A mapping of information to distributed query ids (for which the
	 *         information are part of).
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
	 * @return The stored information or {@link Optional#absent()}, if there is
	 *         no information stored.
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
	public void addBuddy(PeerID peerId, ID sharedQueryId);

	/**
	 * Gives you the map of peers you are the buddy for and the id's of the
	 * shared queries you have to handle if the peer fails
	 * 
	 * @return The Map of peers you are the buddy for
	 */
	public Map<PeerID, List<ID>> getBuddyList();

}