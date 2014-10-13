package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;

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
	 * The mapping of backup information to shared queries.
	 */
	private final Map<ID, Collection<IRecoveryBackupInformation>> mInfos = Maps
			.newHashMap();

	@Override
	public void add(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);

		if (this.mInfos.containsKey(info.getSharedQuery())) {

			this.mInfos.get(info.getSharedQuery()).add(info);

		} else {

			Collection<IRecoveryBackupInformation> infos = Sets
					.newHashSet(info);
			this.mInfos.put(info.getSharedQuery(), infos);

		}

	}

	@Override
	public ImmutableMap<ID, Collection<IRecoveryBackupInformation>> getAll() {

		return ImmutableMap.copyOf(this.mInfos);

	}

	@Override
	public Optional<IRecoveryBackupInformation> get(String pql) {

		Preconditions.checkNotNull(pql);

		for (ID sharedQuery : this.mInfos.keySet()) {

			for (IRecoveryBackupInformation info : this.mInfos.get(sharedQuery)) {

				if (info.getPQL().equals(pql)) {

					return Optional.of(info);

				}

			}

		}

		return Optional.absent();

	}

	@Override
	public ImmutableCollection<IRecoveryBackupInformation> get(ID sharedQuery) {

		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkArgument(this.mInfos.containsKey(sharedQuery));
		return ImmutableSet.copyOf(this.mInfos.get(sharedQuery));

	}

	@Override
	public void remove(ID sharedQuery) {

		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkArgument(this.mInfos.containsKey(sharedQuery));
		this.mInfos.remove(sharedQuery);

	}

	@Override
	public void remove(String pql) {

		Preconditions.checkNotNull(pql);

		for (ID sharedQuery : this.mInfos.keySet().toArray(
				new ID[this.mInfos.keySet().size()])) {

			for (IRecoveryBackupInformation info : this.mInfos.get(sharedQuery)) {

				if (info.getPQL().equals(pql)) {

					this.mInfos.remove(info);

				}

			}

		}

	}

	@Override
	public void remove(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);

		for (ID sharedQuery : this.mInfos.keySet().toArray(
				new ID[this.mInfos.keySet().size()])) {

			this.mInfos.get(sharedQuery).remove(info);

		}

	}

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