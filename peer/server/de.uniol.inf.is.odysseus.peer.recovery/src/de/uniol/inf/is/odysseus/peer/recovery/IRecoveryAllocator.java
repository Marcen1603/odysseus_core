package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import net.jxta.peer.PeerID;

public interface IRecoveryAllocator extends INamedInterface{
	/**
	 * This interface provides methods to allocate queries of failed peers to
	 * new peers. The difference to the Load-Balancing allocation is that all
	 * query parts of a query on a failed peer are transmitted to the new peer.
	 * 
	 * @author Simon Kuespert
	 */

	/**
	 * Allocates queries of failed peers to a new peer.
	 * 
	 * @param knownRemotePeers
	 *            A collection of all known peers identified by their
	 *            {@link PeerID}s. This collection does not contain the local
	 *            peer.
	 * @param localPeerID
	 *            The {@link PeerID} identifying the local peer.
	 */
	public PeerID allocate(Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException;

	/**
	 * Reallocates queries of failed peers to a new peer. This method may be
	 * used, if a first allocation fails.
	 * 
	 * @param faultPeers
	 *            A collection of peers identified by their {@link PeerID}s,
	 *            where the first allocation failed.
	 * @param knownRemotePeers
	 *            A collection of all known peers identified by their
	 *            {@link PeerID}s. This collection does not contain the local
	 *            peer.
	 * @param localPeerID
	 *            The {@link PeerID} identifying the local peer.
	 */
	public PeerID reallocate(Collection<PeerID> faultPeers,
			Collection<PeerID> knownRemotePeers, PeerID localPeerID)
			throws QueryPartAllocationException;
}
