package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;

/**
 * A backup information store can be used to store backup information for
 * recovery techniques. For a shared (distributed) query, it stores the query
 * parts (their pql statements) and the allocated peers.
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
	 * The stored information mapped to their shared query (it's id).
	 */
	private final Map<ID, BackupInformation> mInfoMap = Maps.newHashMap();

	@Override
	public boolean addSharedQuery(ID sharedQueryId,
			Map<PeerID, Collection<String>> pqlStatementsMap) {

		Preconditions.checkNotNull(sharedQueryId,
				"The id of the shared query to store must be not null!");
		Preconditions
				.checkNotNull(pqlStatementsMap,
						"The mapping of pql statements to their allocated peers must be not null!");
		Preconditions
				.checkArgument(
						!pqlStatementsMap.isEmpty(),
						"The mapping of pql statements to their allocated peers must contain at least one entry!");

		if (this.mInfoMap.containsKey(sharedQueryId)) {

			// already stored
			return false;

		}

		this.mInfoMap.put(sharedQueryId,
				new BackupInformation(pqlStatementsMap));
		LOG.debug("Stored backup information about '{}': {}", sharedQueryId,
				pqlStatementsMap);
		return true;

	}

	@Override
	public boolean addPQLStatement(ID sharedQueryId, PeerID peerId,
			String pqlStatement) {

		Preconditions
				.checkNotNull(sharedQueryId,
						"The id of the shared query to store backup information must be not null!");
		Preconditions
				.checkNotNull(peerId,
						"The id of the allocated peer to storebackup information must be not null!");
		Preconditions.checkNotNull(pqlStatement,
				"The pql statement to store must be not null!");

		if (!this.mInfoMap.containsKey(sharedQueryId)) {

			return false;

		} else if (!this.mInfoMap.get(sharedQueryId).containsKey(peerId)) {

			Collection<String> pqlStatements = Lists.newArrayList(pqlStatement);
			this.mInfoMap.get(sharedQueryId).put(peerId, pqlStatements);

		} else {

			this.mInfoMap.get(sharedQueryId).get(peerId).add(pqlStatement);

		}

		return true;

	}

	@Override
	public ImmutableCollection<ID> getStoredSharedQueries() {

		return ImmutableSet.copyOf(this.mInfoMap.keySet());

	}

	@Override
	public ImmutableCollection<PeerID> getStoredPeers(ID sharedQueryId) {

		Preconditions
				.checkNotNull(
						sharedQueryId,
						"The id of the shared query to retrieve stored backup information must be not null!");

		if (!this.mInfoMap.containsKey(sharedQueryId)) {

			Set<PeerID> peers = Sets.newHashSet();
			return ImmutableSet.copyOf(peers);

		}

		return ImmutableSet.copyOf(this.mInfoMap.get(sharedQueryId).keySet());

	}

	@Override
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId,
			PeerID peerId) {

		Preconditions
				.checkNotNull(
						sharedQueryId,
						"The id of the shared query to retrieve stored backup information must be not null!");
		Preconditions
				.checkNotNull(
						peerId,
						"The id of the allocated peer to retrieve stored backup information must be not null!");

		if (!this.mInfoMap.containsKey(sharedQueryId)
				|| !this.mInfoMap.get(sharedQueryId).containsKey(peerId)) {

			Set<String> pqlStatements = Sets.newHashSet();
			return ImmutableSet.copyOf(pqlStatements);

		}

		return ImmutableSet
				.copyOf(this.mInfoMap.get(sharedQueryId).get(peerId));

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

	@Override
	public boolean removePQLStatement(ID sharedQueryId, PeerID peerId,
			String pqlStatement) {

		Preconditions
				.checkNotNull(sharedQueryId,
						"The id of the shared query to remove backup information must be not null!");
		Preconditions
				.checkNotNull(peerId,
						"The id of the allocated peer to remove backup information must be not null!");
		Preconditions.checkNotNull(pqlStatement,
				"The pql statement to remove must be not null!");

		if (!this.mInfoMap.containsKey(sharedQueryId)
				|| !this.mInfoMap.get(sharedQueryId).containsKey(peerId)) {

			return false;

		}

		return this.mInfoMap.get(sharedQueryId).get(peerId)
				.remove(pqlStatement);

	}

}