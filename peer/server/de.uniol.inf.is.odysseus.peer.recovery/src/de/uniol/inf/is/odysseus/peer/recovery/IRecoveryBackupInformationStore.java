package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;

/**
 * The backup information store for recovery. <br />
 * It stores for distributed queries information about local executed (partial)
 * queries and subsequent partial queries.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryBackupInformationStore {

	/**
	 * Adds new backup information.
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
	 * @return <code>True</code>, if the backup information has been stored. <br />
	 *         <code>False</code> if there are backup information about that
	 *         query already stored.
	 */
	public boolean add(ID sharedQueryId, String pqlStatement, PeerID peer,
			Map<String, PeerID> subsequentParts);

	/**
	 * Removes backup information about a given PQL statement.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to remove. <br />
	 *            Must be not null.
	 * @return <code>True</code>, if the backup information has been removed. <br />
	 *         <code>False</code> if there are no backup information about that
	 *         query stored.
	 */
	public boolean remove(ID sharedQueryId, String pqlStatement);

	/**
	 * All shared queries, for which backup information is stored.
	 */
	public ImmutableCollection<ID> getStoredSharedQueries();

	/**
	 * All PQL statements of a distributed query.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 */
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId);

	/**
	 * The peer executing a given statement.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 */
	public Optional<PeerID> getStoredPeerID(ID sharedQueryId,
			String pqlStatement);

	/**
	 * The subsequent parts of a given PQL statement.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 */
	public ImmutableMap<String, PeerID> getStoredSubsequentQueryParts(
			ID sharedQueryId, String pqlStatement);

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
	 * All jxta-information that is stored for a given peer
	 * 
	 * @param peerId
	 *            The id of the peer you want to have the information about
	 * @return A list of jxta-information about the given peer
	 */
	public List<JxtaInformation> getJxtaInfoForPeer(PeerID peerId);

	/**
	 * 
	 * @return All PeerIDs for which jxta-backup-information is stored
	 */
	public Set<PeerID> getPeersFromJxtaInfoStore();

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