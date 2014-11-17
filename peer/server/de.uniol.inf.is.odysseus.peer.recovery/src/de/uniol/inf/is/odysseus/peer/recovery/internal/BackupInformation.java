package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;

/**
 * A backup information is for query parts, which are executed on another peer. <br />
 * An information consists of the following:<br />
 * - id of the distributed query <br />
 * - the PQL code of the query part <br />
 * - the id of the peer, where the query part is executed <br />
 * - information about subsequent parts (relative to the query part): PQL code
 * and peer, where it is executed <br />
 * - the PQL code of the local part for which the backup information are.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformation implements IRecoveryBackupInformation {

	/**
	 * The id of the distributed query.
	 */
	private ID mSharedQuery;

	@Override
	public void setSharedQuery(ID sharedQuery) {

		Preconditions.checkNotNull(sharedQuery);
		this.mSharedQuery = sharedQuery;

	}

	@Override
	public ID getSharedQuery() {

		return this.mSharedQuery;

	}

	/**
	 * The PQL code of the query part.
	 */
	private String mPQL;

	@Override
	public void setPQL(String pql) {

		Preconditions.checkNotNull(pql);
		this.mPQL = pql;

	}

	@Override
	public String getPQL() {

		return this.mPQL;

	}

	/**
	 * The id of the peer where the query part is executed. The PQL is about
	 * this peer.
	 */
	private PeerID mAboutPeer;

	@Override
	public void setAboutPeer(PeerID peer) {
		this.mAboutPeer = peer;
	}

	@Override
	public PeerID getAboutPeer() {
		return this.mAboutPeer;
	}

	/**
	 * The id of the peer where this backup-information originally is located.
	 * This is not necessary the peer where the information is located right
	 * now, because sometimes a peer needs the backup-information of another
	 * peer to restore this information in case of recovery.
	 */
	private PeerID mLocationPeer;

	public PeerID getLocationPeer() {
		return mLocationPeer;
	}

	public void setLocationPeer(PeerID locationPeer) {
		this.mLocationPeer = locationPeer;
	}

	/**
	 * The information about subsequent parts.
	 */
	private Collection<IRecoveryBackupInformation> mSubsequentPartsInformation = Sets.newHashSet();

	@Override
	public ImmutableCollection<IRecoveryBackupInformation> getSubsequentPartsInformation() {

		return ImmutableSet.copyOf(this.mSubsequentPartsInformation);

	}

	@Override
	public void setSubsequentPartsInformation(Collection<IRecoveryBackupInformation> info) {

		Preconditions.checkNotNull(info);
		this.mSubsequentPartsInformation = info;

	}

	@Override
	public void removeSubsequentPartsInformation(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		Preconditions.checkArgument(this.mSubsequentPartsInformation.contains(info));
		this.mSubsequentPartsInformation.remove(info);

	}

	@Override
	public void addSubsequentPartsInformation(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		Preconditions.checkArgument(!this.mSubsequentPartsInformation.contains(info));
		this.mSubsequentPartsInformation.add(info);

	}

	/**
	 * The PQL code of the local part for which the backup information are.
	 */
	private String mLocalPQL;

	@Override
	public void setLocalPQL(String pql) {

		Preconditions.checkNotNull(pql);
		this.mLocalPQL = pql;

	}

	@Override
	public String getLocalPQL() {

		return this.mLocalPQL;

	}

}