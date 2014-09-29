package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;

/**
 * A backup information store can be used to store backup information for
 * recovery techniques. For a shared (distributed) query, it stores the query
 * parts (their pql statements) and the allocated peers.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryBackupInformationStore {

	/**
	 * Adds new backup information for a shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @param pqlStatementsMap
	 *            A mapping of query parts (their pql statements) to the
	 *            allocated peers.
	 * @return <code>True</code>, if the backup information has been stored. <br />
	 *         <code>False</code> if there are backup information about that
	 *         query already stored.
	 */
	public boolean addSharedQuery(ID sharedQueryId,
			Map<PeerID, Collection<String>> pqlStatementsMap);

	/**
	 * Adds a new pql statement to the backup information for a shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @param peerId
	 *            The id of the allocated peer. <br />
	 *            Must be not null.
	 * @param pqlStatement
	 *            The pql statement to store.
	 * @return <code>True</code>, if the pql statement has been stored. <br />
	 *         <code>False</code> if there are no backup information about that
	 *         query stored.
	 */
	public boolean addPQLStatement(ID sharedQueryId, PeerID peerId,
			String pqlStatement);

	/**
	 * Adds a new entry for the jxta-backup information. E.g. pipe-ids
	 * 
	 * @param peerId
	 *            Id of the peer about which the information is
	 * @param sharedQueryId
	 *            Id of the shared query about which the information is
	 * @param key
	 *            Key of the key-value-pair to save and access this information
	 *            (e.g. "receiverPipeId")
	 * @param value
	 *            Value of the key-value-pair. E.g. the pipe-id of a
	 *            jxta-receiver
	 * @return <code>True</code>, if the information has been stored
	 *         <code>False</code> if the information was not stored
	 */
	public boolean addJxtaInfo(PeerID peerId, ID sharedQueryId, String key,
			String value);

	/**
	 * All shared queries, for which backup information is stored.
	 * 
	 * @return A collection of shared queries (their ids).
	 */
	public ImmutableCollection<ID> getStoredSharedQueries();

	/**
	 * All peers, which are part of a given shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @return A collection of all peers of the shared query.
	 */
	public ImmutableCollection<PeerID> getStoredPeers(ID sharedQueryId);

	/**
	 * All pql statements, which are part of a given shared query and allocated
	 * to a given peer.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @param peerId
	 *            The id of the allocated peer. <br />
	 *            Must be not null.
	 * @return A collection of all pql statement of the shared query allocated
	 *         to the given peer.
	 */
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId,
			PeerID peerId);
	
	/**
	 * All jxta-information that is stored for a given peer
	 * @param peerId The id of the peer you want to have the information about
	 * @return A list of jxta-information about the given peer
	 */
	public List<JxtaInformation> getJxtaInfoForPeer(PeerID peerId);

	/**
	 * 
	 * @return All PeerIDs for which jxta-backup-information is stored
	 */
	public Set<PeerID> getPeersFromJxtaInfoStore();
	
	/**
	 * Removes a backup information for a shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @return <code>True</code>, if the backup information has been removed. <br />
	 *         <code>False</code> if there are no backup information about that
	 *         query stored.
	 */
	public boolean removeStoredSharedQuery(ID sharedQueryId);

	/**
	 * Removes a pql statement from the backup information of a shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @param peerId
	 *            The id of the allocated peer. <br />
	 *            Must be not null.
	 * @param pqlStatement
	 *            The pql statement to remove.
	 * @return <code>True</code>, if the pql statement has been removed. <br />
	 *         <code>False</code> if there are no backup information about that
	 *         query stored.
	 */
	public boolean removePQLStatement(ID sharedQueryId, PeerID peerId,
			String pqlStatement);

}