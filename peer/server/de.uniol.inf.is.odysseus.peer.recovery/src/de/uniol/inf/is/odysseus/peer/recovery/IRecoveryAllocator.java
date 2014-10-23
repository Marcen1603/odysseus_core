package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IRecoveryAllocator extends INamedInterface{
	/**
	 * This interface provides methods to allocate queries of failed peers to
	 * new peers. Follows the same pattern like the load-balancing allocator.
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
	
	// TODO The code above will be replaced	by the following:
	
//	/**
//	 * Allocates a given collection of {@link ILogicalQueryPart}s to given peers.
//	 * @param queryParts The collection of {@link ILogicalQueryPart}s to allocate.
//	 * @param query The complete {@link ILogicalQuery} of <code>queryParts</code>, so all {@link ILogicalQueryPart}s must belong to the same {@link ILogicalQuery}.
//	 * @param knownRemotePeers A collection of all known peers identified by their {@link PeerID}s. This collection does not contain the local peer.
//	 * @param localPeerID The {@link PeerID} identifying the local peer.
//	 * @return A mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
//	 * @throws QueryPartAllocationException if any error occurs.
//	 */
//	public Map<ILogicalQueryPart, PeerID> allocate(
//			Collection<ILogicalQueryPart> queryParts, 
//			ILogicalQuery query, Collection<PeerID> knownRemotePeers, 
//			PeerID localPeerID) throws QueryPartAllocationException;
//	
//	/**
//	 * Reallocates a given mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s. <br />
//	 * This method may be used, if a first allocation ({@link #allocate(Collection, ILogicalQuery, Collection, PeerID)}) fails.
//	 * @param previousAllocationMap A previous mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
//	 * @param faultPeers A collection of peers identified by their {@link PeerID}s, where the first allocation failed.
//	 * @param knownRemotePeers A collection of all known peers identified by their {@link PeerID}s. This collection does not contain the local peer.
//	 * @param localPeerID The {@link PeerID} identifying the local peer.
//	 * @return A mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
//	 * @throws QueryPartAllocationException if any error occurs.
//	 */
//	public Map<ILogicalQueryPart, PeerID> reallocate(
//			Map<ILogicalQueryPart, PeerID> previousAllocationMap, 
//			Collection<PeerID> faultPeers, 
//			Collection<PeerID> knownRemotePeers, 
//			PeerID localPeerID) throws QueryPartAllocationException;
}
