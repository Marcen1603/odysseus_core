package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;

/**
 * A backup information store can be used to store backup information for
 * recovery techniques. For a shared (distributed) query, it stores the pql
 * strings of all query parts.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationStore implements IRecoveryBackupInformationStore {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationStore.class);

	/**
	 * The stored information as a mapping of pql statements to their shared
	 * query (it's id).
	 */
	private final Map<ID, ImmutableCollection<String>> mInfoMap = Maps
			.newHashMap();

	@Override
	public boolean addSharedQuery(ID sharedQueryId,
			Collection<String> pqlStatements) {

		Preconditions.checkNotNull(sharedQueryId,
				"The id of the shared query to store must be not null!");
		Preconditions.checkNotNull(
				pqlStatements,
				"The pql statements of the shared query '"
						+ sharedQueryId.toString() + "' must be not null!");
		Preconditions.checkArgument(!pqlStatements.isEmpty(),
				"There must be at leat one pql statement of the shared query '"
						+ sharedQueryId.toString() + "'!");

		if (this.mInfoMap.containsKey(sharedQueryId)) {

			// already stored
			return false;

		}

		this.mInfoMap.put(sharedQueryId, ImmutableSet.copyOf(pqlStatements));
		LOG.debug("Stored backup information about '{}': {}", sharedQueryId,
				pqlStatements);
		return true;

	}

	@Override
	public ImmutableCollection<ID> getStoredSharedQueries() {

		return ImmutableSet.copyOf(this.mInfoMap.keySet());

	}

	@Override
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId) {

		Preconditions
				.checkNotNull(
						sharedQueryId,
						"The id of the shared query to retrieve stored backup information must be not null!");

		if (!this.mInfoMap.containsKey(sharedQueryId)) {

			Set<String> parts = Sets.newHashSet();
			return ImmutableSet.copyOf(parts);

		}

		return this.mInfoMap.get(sharedQueryId);

	}

	@Override
	public boolean removeStoredSharedQuery(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId,
				"The id of the shared query to store must be not null!");

		if (this.mInfoMap.remove(sharedQueryId) != null) {
			LOG.debug("Removed backup information about '{}'.", sharedQueryId);

		}
		return true;

	}

}