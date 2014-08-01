package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

/**
 * The {@link ILoadBalancingAllocator} interface provides methods to allocate {@link ILogicalQueryPart}s to peers. <br />
 * This interface is similar to {@link IQueryPartAllocator}. The {@link IQueryPartAllocator} is used for static allocation within the distribution process. 
 * The {@link ILoadBalancingAllocator} is used for dynamic allocation within the load balancing process. <br />
 * However, it is possible to implement concrete allocation strategies implementing both interfaces.
 * @author Michael Brand
 */
public interface ILoadBalancingAllocator extends INamedInterface {
	
	/**
	 * Allocates a given collection of {@link ILogicalQueryPart}s to given peers.
	 * @param queryParts The collection of {@link ILogicalQueryPart}s to allocate.
	 * @param query The complete {@link ILogicalQuery} of <code>queryParts</code>, so all {@link ILogicalQueryPart}s must belong to the same {@link ILogicalQuery}.
	 * @param knownRemotePeers A collection of all known peers identified by their {@link PeerID}s. This collection does not contain the local peer.
	 * @param localPeerID The {@link PeerID} identifying the local peer.
	 * @param config The {@link QueryBuildConfiguration} containing Odysseus-Script parameters.
	 * @param allocatorParameters A list of all parameters for the allocator.
	 * @return A mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
	 * @throws QueryPartAllocationException if any error occurs.
	 */
	public Map<ILogicalQueryPart, PeerID> allocate(
			Collection<ILogicalQueryPart> queryParts, 
			ILogicalQuery query, Collection<PeerID> knownRemotePeers, 
			PeerID localPeerID, 
			QueryBuildConfiguration config, 
			List<String> allocatorParameters) throws QueryPartAllocationException;
	
	/**
	 * Reallocates a given mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s. <br />
	 * This method may be used, if a first allocation ({@link #allocate(Collection, ILogicalQuery, Collection, PeerID, QueryBuildConfiguration, List)}) fails.
	 * @param previousAllocationMap A previous mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
	 * @param faultPeers A collection of peers identified by their {@link PeerID}s, where the first allocation failed.
	 * @param knownRemotePeers A collection of all known peers identified by their {@link PeerID}s. This collection does not contain the local peer.
	 * @param localPeerID The {@link PeerID} identifying the local peer.
	 * @param config The {@link QueryBuildConfiguration} containing Odysseus-Script parameters.
	 * @param allocatorParameters A list of all parameters for the allocator.
	 * @return A mapping of {@link PeerID}s (each identifying a peer) to {@link ILogicalQueryPart}s, indicating which {@link ILogicalQueryPart} has been allocated to which peer.
	 * @throws QueryPartAllocationException if any error occurs.
	 */
	public Map<ILogicalQueryPart, PeerID> reallocate(
			Map<ILogicalQueryPart, PeerID> previousAllocationMap, 
			Collection<PeerID> faultPeers, 
			Collection<PeerID> knownRemotePeers, 
			PeerID localPeerID, 
			QueryBuildConfiguration config, 
			List<String> allocatorParameters ) throws QueryPartAllocationException;

}