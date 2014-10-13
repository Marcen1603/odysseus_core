package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;

/**
 * A backup information is for query parts, which are executed on another peer. <br />
 * An information consists of the following:<br />
 * - id of the distributed query <br />
 * - the PQL code of the query part <br />
 * - the id of the peer, where the query part is executed <br />
 * - information about subsequent parts (relative to the query part): PQL code
 * and peer, where it is executed.
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
	private String m_PQL;

	@Override
	public void setPQL(String pql) {

		Preconditions.checkNotNull(pql);
		this.m_PQL = pql;

	}

	@Override
	public String getPQL() {

		return this.m_PQL;

	}

	/**
	 * The id of the peer, where the query part is executed.
	 */
	private PeerID mPeer;

	@Override
	public void setPeer(PeerID peer) {

		Preconditions.checkNotNull(peer);
		this.mPeer = peer;

	}

	@Override
	public PeerID getPeer() {

		return this.mPeer;

	}

	/**
	 * The information about subsequent parts (relative to the query part): PQL
	 * code and peer, where it is executed.
	 */
	private Collection<IPair<String, PeerID>> mSubsequentPartsInformation = Sets
			.newHashSet();

	@Override
	public ImmutableCollection<IPair<String, PeerID>> getSubsequentPartsInformation() {

		return ImmutableSet.copyOf(this.mSubsequentPartsInformation);

	}

	@Override
	public void setSubsequentPartsInformation(
			Collection<IPair<String, PeerID>> info) {

		Preconditions.checkNotNull(info);
		this.mSubsequentPartsInformation = info;

	}

	@Override
	public void removeSubsequentPartsInformation(IPair<String, PeerID> info) {

		Preconditions.checkNotNull(info);
		Preconditions.checkArgument(this.mSubsequentPartsInformation
				.contains(info));
		this.mSubsequentPartsInformation.remove(info);

	}

	@Override
	public void addSubsequentPartsInformation(IPair<String, PeerID> info) {

		Preconditions.checkNotNull(info);
		Preconditions.checkArgument(!this.mSubsequentPartsInformation
				.contains(info));
		this.mSubsequentPartsInformation.add(info);

	}

}