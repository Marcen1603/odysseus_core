package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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
 * and peer, where it is executed <br />
 * - the PQL code of the local part for which the backup information are.
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
	private final Map<ID, Collection<IRecoveryBackupInformation>> mInfos = Maps.newHashMap();

	@Override
	public void add(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);

		if (this.mInfos.containsKey(info.getSharedQuery())) {

			for (IRecoveryBackupInformation backupInfo : this.mInfos.get(info.getSharedQuery())) {
				
				if (backupInfo.equals(info))
					return;
				
				if (info.getPQL() != null && backupInfo.getPQL() != null) {
					String comparePQL1 = backupInfo.getPQL().trim().toLowerCase();
					String comparePQL2 = info.getPQL().trim().toLowerCase();

					if (comparePQL1.equals(comparePQL2)) {
						// If we already have this PQl -> don't save it twice
						
						// But maybe the other information which is in this message has to be merges into this backupInfo
						if (Strings.isNullOrEmpty(backupInfo.getLocalPQL()) && !Strings.isNullOrEmpty(info.getLocalPQL())) {
							if (backupInfo.getLocationPeer() == null && info.getLocationPeer() != null) {
								backupInfo.setLocalPQL(info.getLocalPQL());
								backupInfo.setLocationPeer(info.getLocationPeer());
							}
						}
							
						return;
					}
				}
				if (info.getLocalPQL() != null && backupInfo.getLocalPQL() != null) {
					String comparePQL1 = backupInfo.getLocalPQL().trim().toLowerCase();
					String comparePQL2 = info.getLocalPQL().trim().toLowerCase();

					if (comparePQL1.equals(comparePQL2)) {
						// If we already have this local PQL -> don't save it twice
						
						// But maybe the other information which is in this message has to be merges into this backupInfo
						if (Strings.isNullOrEmpty(backupInfo.getPQL()) && !Strings.isNullOrEmpty(info.getPQL())) {
							if (backupInfo.getAboutPeer() == null && info.getAboutPeer() != null) {
								backupInfo.setPQL(info.getPQL());
								backupInfo.setAboutPeer(info.getAboutPeer());
							}
						}
						return;
					}
				}
			}

			this.mInfos.get(info.getSharedQuery()).add(info);

		} else {

			Collection<IRecoveryBackupInformation> infos = Sets.newHashSet(info);
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
		this.mInfos.remove(sharedQuery);

	}

	@Override
	public void remove(String pql) {

		Preconditions.checkNotNull(pql);

		for (ID sharedQuery : this.mInfos.keySet().toArray(new ID[this.mInfos.keySet().size()])) {

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

		for (ID sharedQuery : this.mInfos.keySet().toArray(new ID[this.mInfos.keySet().size()])) {

			this.mInfos.get(sharedQuery).remove(info);

		}

	}
	
	@Override
	public void remove(ID sharedQueryId, PeerID aboutPeerId) {
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(aboutPeerId);
		for (IRecoveryBackupInformation info : this.mInfos.get(sharedQueryId)) {
			if (info.getAboutPeer().equals(aboutPeerId)) {
				this.mInfos.get(sharedQueryId).remove(info);
			}
		}
	}

	/*
	 * Buddy-information (the peers for which we are the buddy)
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

	/*
	 * My buddies (the peers which are my buddies)
	 */

	/**
	 * The stored information which peers are my buddies
	 */
	private final Map<PeerID, List<ID>> mMyBuddyMap = Maps.newHashMap();

	@Override
	public void addMyBuddy(PeerID peerId, ID sharedQueryId) {
		List<ID> queryList = null;
		if (!mMyBuddyMap.containsKey(peerId)) {
			queryList = new ArrayList<ID>();
		} else {
			queryList = mMyBuddyMap.get(peerId);
		}

		queryList.add(sharedQueryId);
		mMyBuddyMap.put(peerId, queryList);
	}

	@Override
	public List<ID> removeMyBuddy(PeerID peerId) {
		return mMyBuddyMap.remove(peerId);
	}

	@Override
	public Map<PeerID, List<ID>> getMyBuddyList() {
		ImmutableMap<PeerID, List<ID>> copyMap = ImmutableMap.copyOf(mMyBuddyMap);
		return copyMap;
	}

}