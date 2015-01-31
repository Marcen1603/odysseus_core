package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

/***
 * Query Part Controller is responsible for managing relations between Query Parts.
 * e.g. If a Query Part is Master, removing this part would also remove all slaves of this Master.
 *
 */
public interface IQueryPartController {

	/**
	 * Returns shared QueryID for local Query.
	 * @param queryId local Query ID
	 * @return shared Query ID
	 */
	public ID getSharedQueryID(int queryId);

	/***
	 * Gets all local Query IDs for shared Query ID
	 * @param sharedQueryId shared Query ID
	 * @return Collection of local Queries
	 */
	public Collection<Integer> getLocalIds(ID sharedQueryId);

	/***
	 * Registers a Query as Slave of another Query
	 * @param ids
	 * @param sharedQueryID
	 * @param masterPeer
	 */
	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID, PeerID masterPeer);
	
	/**
	 * Registers a Query as Master for other Queries
	 * @param query Logical Query to register
	 * @param queryID id of logical Query
	 * @param sharedQueryID Shared Query ID to register
	 * @param otherPeers Collection of Slave Peers (without master)
	 */
	public void registerAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers);
	
	/**
	 * Unregisters a Query as Master from a shared Query ID. (Local Queries still remain, this would make a Master Peer a Slave)
	 * @param sharedQueryId shared Query ID to unregister Master
	 */
	public void unregisterAsMaster(ID sharedQueryId);
	
	/**
	 * True if Query is master Query
	 * @param queryID local Query ID to look for.
	 * @return
	 */
	public boolean isMasterForQuery(int queryID);
	
	/***
	 * True if shared Query ID is known on peer.
	 * @param sharedQueryID SharedQueryID
	 * @return
	 */
	public boolean isSharedQueryKnown(ID sharedQueryID);
	
	/***
	 * Returns Peer ID of Master peer for shared Query ID.
	 * @param sharedQueryID
	 * @return
	 */
	public PeerID getMasterForQuery(ID sharedQueryID);
	
	/**
	 * Get all slave Peers for shared Query ID (Only on master Peer!)
	 * @param sharedQueryId
	 * @return
	 */
	public Collection<PeerID> getOtherPeers(ID sharedQueryId);
	
	/**
	 * Adds slave peer to Master Query.
	 * @param sharedQueryId shared Query ID to add Peer to
	 * @param peerID Peer ID of slave
	 * @return
	 */
	public PeerID addOtherPeer(ID sharedQueryId, PeerID peerID);
	
	/***
	 * Removes Slave Peer from shared Query.
	 * @param sharedQueryId shared Query ID
	 * @param peerID Peer ID of Slave to remove.
	 */
	public void removeOtherPeer(ID sharedQueryId, PeerID peerID);
	
	/**
	 * Unregisters a Local Query from a shared Query
	 * @param sharedQueryID SharedQuery ID
	 * @param toRemove Collection of logical Query IDs to remove.
	 */
	public void unregisterLocalQueriesFromSharedQuery(ID sharedQueryID, Collection<Integer> toRemove);

	/***
	 * Adds a local Query to shared Query ID.
	 * @param sharedQueryID shared Query ID
	 * @param localQueryID local Query ID to add.
	 */
	void addLocalQueryToShared(ID sharedQueryID, int localQueryID);

	
	/**
	 * Returns all shared query IDs
	 * @return
	 */
	Collection<ID> getSharedQueryIds();
	
}