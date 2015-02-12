package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;

/**
 * Gives you access to the backup-information (e.g. for recovery)
 * 
 * @author Tobias Brandt
 *
 */
public interface IBackupInformationAccess {

	/**
	 * Saves the backup-information and shares it with the other peers.
	 * 
	 * @param queryId
	 *            The local query id of this query
	 * @param pql
	 *            The PQL string of this query
	 * @param state
	 *            The state of the query, e.g. "running".
	 * @param sharedQuery
	 *            The shared query id as a string.
	 */
	public void saveBackupInformation(int queryId, String pql, String state, String sharedQuery, boolean master,
			String masterId);

	/**
	 * Saves the backup-information for another peer and shares it with the other peers.
	 * 
	 * @param peerId
	 *            The id of the other peer as string.
	 * @param queryId
	 *            The local query id of this query
	 * @param pql
	 *            The PQL string of this query
	 * @param state
	 *            The state of the query, e.g. "running".
	 * @param sharedQuery
	 *            The shared query id as a string.
	 */
	public void saveBackupInformation(String peerId, int queryId, String pql, String state, String sharedQuery,
			boolean master, String masterId);

	/**
	 * Adds information about a socket connection for that query.
	 * 
	 * @param queryId
	 *            The id of the query for which this socket is there
	 * @param clientIp
	 *            The client ip address
	 */
	public void addSocketInfoForQuery(int queryId, String clientIp);

	/**
	 * Removes a backup-information for this peer by the query id.
	 * 
	 * @param queryId
	 *            The local id of the query which was removed
	 */
	public void removeBackupInformation(int queryId);

	/**
	 * Removes a backup-information about the given peer and the given peer id
	 * 
	 * @param peerId
	 *            The peer id for which the backup-information needs to be removed
	 * @param queryId
	 *            The local query id of the query (local on the other peer)
	 */
	public void removeBackupInformation(String peerId, int queryId);

	/**
	 * Updates the backup-information of JxtaSender or JxtaReceiver with that pipeId. Replaces the old (failed) peer id
	 * with the one from this peer. This is necessary for the case that a JxtaSender - JxtaReceiper-pair was on the
	 * same, failed peer. It wouldn't be possible to install the JxtaReceiver with a peerId which is not reachable.
	 * 
	 * @param pipeId
	 * @param iHaveSender
	 */
	public void updateBackupInfoForPipe(String failedPeer, String pipeId);

	/**
	 * 
	 * @return A list of the peer IDs we know and we have information about
	 */
	public ArrayList<String> getBackupPeerIds();

	/**
	 * 
	 * @param peerId
	 *            The peer id you want to have all the backup-information about
	 * @return A Map of all backup-information we have about this peer. The key is the local query id and be value is
	 *         the detailed information about that query.
	 */
	public HashMap<Integer, BackupInfo> getBackupInformation(String peerId);

	/**
	 * 
	 * @param peerId
	 *            The peer you want to know the PQL about
	 * @param queryId
	 *            The local query id on that peer for the query you want to know the PQL about
	 * @return The PQL code as String for the given query id for the given peer
	 */
	public String getBackupPQL(String peerId, int queryId);

	/**
	 * 
	 * @param queryId
	 *            The query id for which you want to know the PQL
	 * @return The PQL String for the query with the given id on this peer
	 */
	public String getBackupPQL(int queryId);

	public String getBackupSharedQuery(int queryId);

	public boolean isBackupMaster(int queryId);

	public String getBackupMasterId(int queryId);

	/**
	 * 
	 * @return The Map of backup-information for this peer
	 */
	public HashMap<Integer, BackupInfo> getBackupInformation();

	/**
	 * 
	 * @return The Map of backup-information for this peer for a given query id
	 */
	public BackupInfo getBackupInformation(int queryId);

}
