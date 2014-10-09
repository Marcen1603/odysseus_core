package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformation;

/**
 * The backup information store for recovery. <br />
 * It stores for distributed queries information about local executed (partial)
 * queries and subsequent partial queries.
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
	 * The stored backup information.
	 */
	private final Collection<BackupInformation> mBackupInfos = Collections
			.synchronizedCollection(new CopyOnWriteArrayList<BackupInformation>());

	/**
	 * The stored information, for which peers and which shared query (it's id)
	 * we are the buddy
	 */
	private final Map<PeerID, List<ID>> mBuddyMap = Maps.newHashMap();

	/**
	 * Map for additional information mapped to their shared query (it's id).
	 * E.g. for pipe-Ids, etc.
	 */
	private final Map<PeerID, List<JxtaInformation>> mJxtaInfoMap = Maps
			.newHashMap();

	@Override
	public boolean add(ID sharedQueryId, String pqlStatement, PeerID peer,
			Map<String, PeerID> subsequentParts) {

		if (this.containsInfo(sharedQueryId, pqlStatement)) {

			// already stored
			return false;

		}

		BackupInformation info = new BackupInformation(sharedQueryId,
				pqlStatement);
		info.setPeer(peer);
		for (String subsequentPart : subsequentParts.keySet()) {

			info.addSubsequentPart(subsequentPart,
					subsequentParts.get(subsequentPart));

		}

		synchronized (this.mBackupInfos) {

			this.mBackupInfos.add(info);

		}

		LOG.debug("Stored new backup information: {}", info);
		return true;

	}

	@Override
	public boolean remove(ID sharedQueryId, String pqlStatement) {

		for (BackupInformation info : this.findInfos(sharedQueryId)) {

			if (info.getPQLStatement().equals(pqlStatement)) {

				synchronized (this.mBackupInfos) {

					this.mBackupInfos.remove(info);

				}

				LOG.debug("Removed backup information: {}", info);
				return true;

			}

		}

		return false;

	}

	/**
	 * Checks, if there are information stored about a given PQL statement.
	 * sharedQueryId The ID of the distributed query. <br />
	 * Must be not null!
	 * 
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 * @return True, if there are information stored; false, if not.
	 */
	private boolean containsInfo(ID sharedQueryId, String pqlStatement) {

		Preconditions.checkNotNull(sharedQueryId,
				"The shared query ID must be not null!");

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");

		for (BackupInformation info : this.mBackupInfos) {

			if (info.getSharedQueryID().equals(sharedQueryId)
					&& info.getPQLStatement().equals(pqlStatement)) {

				return true;

			}

		}

		return false;

	}

	@Override
	public ImmutableCollection<ID> getStoredSharedQueries() {

		Set<ID> ids = Sets.newHashSet();

		for (BackupInformation info : this.mBackupInfos) {

			if (!ids.contains(info.getSharedQueryID())) {

				ids.add(info.getSharedQueryID());

			}

		}

		return ImmutableSet.copyOf(ids);

	}

	@Override
	public ImmutableCollection<String> getStoredPQLStatements(ID sharedQueryId) {

		Collection<String> statements = Lists.newArrayList();

		for (BackupInformation info : this.findInfos(sharedQueryId)) {

			statements.add(info.getPQLStatement());

		}

		return ImmutableSet.copyOf(statements);

	}

	/**
	 * Gets all backup information about a distributed query.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 */
	private Collection<BackupInformation> findInfos(ID sharedQueryId) {

		Preconditions.checkNotNull(sharedQueryId,
				"The shared query ID must be not null!");

		Collection<BackupInformation> infos = Lists.newArrayList();

		for (BackupInformation info : this.mBackupInfos) {

			if (info.getSharedQueryID().equals(sharedQueryId)) {

				infos.add(info);

			}

		}

		return infos;

	}

	@Override
	public Optional<PeerID> getStoredPeerID(ID sharedQueryId,
			String pqlStatement) {

		for (BackupInformation info : this.findInfos(sharedQueryId)) {

			if (info.getPQLStatement().equals(pqlStatement)) {

				return Optional.of(info.getPeer());

			}

		}

		return Optional.absent();

	}

	@Override
	public ImmutableMap<String, PeerID> getStoredSubsequentQueryParts(
			ID sharedQueryId, String pqlStatement) {

		for (BackupInformation info : this.findInfos(sharedQueryId)) {

			if (info.getPQLStatement().equals(pqlStatement)) {

				return info.getSubsequentParts();

			}

		}

		return ImmutableMap.copyOf(new HashMap<String, PeerID>());

	}

	@Override
	public boolean addJxtaInfo(PeerID peerId, ID sharedQueryId, String key,
			String value) {

		Preconditions
				.checkNotNull(sharedQueryId,
						"The id of the shared query to store backup information must be not null!");
		Preconditions
				.checkNotNull(peerId,
						"The id of the allocated peer to storebackup information must be not null!");

		if (!mJxtaInfoMap.containsKey(peerId)) {
			// We don't have information about this peer -> add List
			List<JxtaInformation> jxtaInfo = new ArrayList<JxtaInformation>();
			mJxtaInfoMap.put(peerId, jxtaInfo);
		}
		JxtaInformation newInfo = new JxtaInformation(sharedQueryId, key, value);
		mJxtaInfoMap.get(peerId).add(newInfo);

		return true;
	}

	@Override
	public List<JxtaInformation> getJxtaInfoForPeer(PeerID peerId) {
		if (!mJxtaInfoMap.containsKey(peerId)) {
			return null;
		}
		return mJxtaInfoMap.get(peerId);
	}

	@Override
	public Set<PeerID> getPeersFromJxtaInfoStore() {
		return mJxtaInfoMap.keySet();
	}

	@Override
	public void addBuddy(PeerID peerId, ID sharedQueryId) {
		List<ID> queryList = null;
		if (!mBuddyMap.containsKey(peerId)) {
			queryList = new ArrayList<ID>();
		} else {
			queryList = mBuddyMap.get(peerId);
		}

		queryList.add(sharedQueryId);
		mBuddyMap.put(peerId, queryList);
	}

	@Override
	public Map<PeerID, List<ID>> getBuddyList() {
		ImmutableMap<PeerID, List<ID>> copyMap = ImmutableMap.copyOf(mBuddyMap);
		return copyMap;
	}

}