package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
 * It stores multiple backup information.
 * 
 * A backup information is for query parts, which are executed on another peer. <br />
 * An information consists of the following:<br />
 * - id of the distributed query <br />
 * - the PQL code of the query part <br />
 * - the id of the peer, where the query part is executed <br />
 * - information about subsequent parts (relative to the query part): PQL code
 * and peer, where it is executed.
 * 
 * The stored information are about query parts, which are subsequent to query
 * parts executed on the local peer.
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

	/*
	 * TODO Buddy information. Not clear atm. if they are needed.
	 */

	/**
	 * The stored information, for which peers and which shared query (it's id)
	 * we are the buddy
	 */
	private final Map<PeerID, List<ID>> mBuddyMap = Maps.newHashMap();

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