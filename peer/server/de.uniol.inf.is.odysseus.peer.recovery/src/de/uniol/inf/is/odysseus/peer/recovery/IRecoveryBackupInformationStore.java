package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;
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

	public void add(IRecoveryBackupInformation info);

	public ImmutableMap<ID, Collection<IRecoveryBackupInformation>> getAll();

	public ImmutableCollection<IRecoveryBackupInformation> get(ID sharedQuery);

	public Optional<IRecoveryBackupInformation> get(String pql);

	public void remove(ID sharedQuery);

	public void remove(IRecoveryBackupInformation info);

	public void remove(String pql);

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