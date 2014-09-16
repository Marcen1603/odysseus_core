package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * A backup information store can be used to store backup information for
 * recovery techniques. For a shared (distributed) query, it stores all query
 * parts.
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
	 * @param queryParts
	 *            A collection of all query parts of the shared query.
	 * @return <code>True</code>, if the backup information has been stored. <br />
	 *         <code>False</code> if there are backup information about that
	 *         query already stored.
	 */
	public boolean addSharedQuery(PeerID sharedQueryId,
			Collection<ILogicalQueryPart> queryParts);

	/**
	 * All shared queries, for which backup information is stored.
	 * 
	 * @return A collection of shared queries (their ids).
	 */
	public ImmutableCollection<PeerID> getStoredSharedQueries();

	/**
	 * All query parts, which are part of a given shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @return A collection of all query parts of the shared query.
	 */
	public ImmutableCollection<ILogicalQueryPart> getStoredQueryParts(
			PeerID sharedQueryId);

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
	public boolean removeStoredSharedQuery(PeerID sharedQueryId);

}