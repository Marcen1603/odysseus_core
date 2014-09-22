package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import net.jxta.id.ID;

import com.google.common.collect.ImmutableCollection;

/**
 * A backup information store can be used to store backup information for
 * recovery techniques. For a shared (distributed) query, it stores the pql
 * strings of all query parts.
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
	 * @param pqlStatements
	 *            A collection of pql statements of all query parts of the
	 *            shared query.
	 * @return <code>True</code>, if the backup information has been stored. <br />
	 *         <code>False</code> if there are backup information about that
	 *         query already stored.
	 */
	public boolean addSharedQuery(ID sharedQueryId,
			Collection<String> pqlStatements);

	/**
	 * All shared queries, for which backup information is stored.
	 * 
	 * @return A collection of shared queries (their ids).
	 */
	public ImmutableCollection<ID> getStoredSharedQueries();

	/**
	 * All pql statements, which are part of a given shared query.
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query. <br />
	 *            Must be not null.
	 * @return A collection of pql statements of all query parts of the shared
	 *         query.
	 */
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId);

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

}