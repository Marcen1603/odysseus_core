package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * The backup information for a shared query is a PQL statement, an ID of the
 * peer, where that statement is installed and a list of all subsequent query
 * parts.
 * 
 * @author Michael Brand
 *
 */
public final class BackupInformation {

	/**
	 * The ID of the distributed query.
	 */
	private final ID mSharedQueryId;

	/**
	 * The stored PQL statement.
	 */
	private final String mPqlStatement;

	/**
	 * The ID of the peer, where the PQL statement is installed.
	 */
	private PeerID mPeerId;

	/**
	 * The subsequent parts for the PQL statement.
	 */
	private final Map<String, PeerID> mSubsequentParts = Maps.newHashMap();

	/**
	 * Creates new backup information about a given PQL statement.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 */
	public BackupInformation(ID sharedQueryId, String pqlStatement) {

		Preconditions.checkNotNull(sharedQueryId,
				"The shared query ID must be not null!");
		this.mSharedQueryId = sharedQueryId;

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		this.mPqlStatement = pqlStatement;

	}

	/**
	 * The ID of the distributed query.
	 */
	public ID getSharedQueryID() {

		return this.mSharedQueryId;

	}

	/**
	 * The stored PQL statement.
	 */
	public String getPQLStatement() {

		return this.mPqlStatement;

	}

	/**
	 * Sets the ID of the peer, where the PQL statement is installed. <br />
	 * <code>peerId</code> must be not null!
	 */
	public void setPeer(PeerID peerId) {

		Preconditions.checkNotNull(peerId, "The peer ID must be not null!");
		this.mPeerId = peerId;

	}

	/**
	 * The ID of the peer, where the PQL statement is installed.
	 */
	public PeerID getPeer() {

		return this.mPeerId;

	}

	/**
	 * Adds a subsequent part for the PQL statement.
	 * 
	 * @param pqlStatement
	 *            The PQL statement of the subsequent part. <br />
	 *            Must be not null!
	 * @param peer
	 *            The ID of the peer, where the subsequent PQL statement is
	 *            installed. <br />
	 *            Must be not null!
	 */
	public void addSubsequentPart(String pqlStatement, PeerID peer) {

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		Preconditions.checkNotNull(peer, "The peer ID must be not null!");
		this.mSubsequentParts.put(pqlStatement, peer);

	}

	/**
	 * Removes a subsequent part for the PQL statement.
	 * 
	 * @param pqlStatement
	 *            The PQL statement of the subsequent part. <br />
	 *            Must be not null!
	 */
	public void removeSubsequentPart(String pqlStatement) {

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		this.mSubsequentParts.remove(pqlStatement);

	}

	/**
	 * The subsequent parts of the PQL statement.
	 * 
	 * @return The PQL statements of the subsequent parts and the IDs of the
	 *         peers, where the subsequent PQL statements are installed.
	 */
	public ImmutableMap<String, PeerID> getSubsequentParts() {

		return ImmutableMap.copyOf(this.mSubsequentParts);

	}

}