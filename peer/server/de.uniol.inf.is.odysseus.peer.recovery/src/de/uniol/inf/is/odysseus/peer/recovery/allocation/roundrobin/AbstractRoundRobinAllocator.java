package de.uniol.inf.is.odysseus.peer.recovery.allocation.roundrobin;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;

/**
 * The abstract round robin allocator holds the round robin algorithm. <br />
 * The peers considered for allocation may differ for concrete implementations.
 * Adaption of {@link de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin.AbstractRoundRobinAllocator}
 * @author Simon Kuespert
 */
public abstract class AbstractRoundRobinAllocator implements IRecoveryAllocator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRoundRobinAllocator.class);
	
	private static IP2PDictionary p2pDictionary;
	
	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {

	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		
	}
	
	@Override
	public PeerID allocate(
			Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException{
		
		LOG.debug("Begin round robin allocation");
		
		if(p2pDictionary == null) {
			
			throw new QueryPartAllocationException("No peer dictionary set");
			
		}
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID);
		if(LOG.isDebugEnabled()) {
			
			logPeers(peerIDs);
			
		}
		
		if(peerIDs == null || peerIDs.isEmpty()) {
			
			throw new QueryPartAllocationException("There are no peers to be considered for round robin");
			
		}
		
		return roundRobinImpl(peerIDs);
		
	}

	@Override
	public PeerID reallocate( 
			Collection<PeerID> faultPeers, 
			Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException {
		
		LOG.debug("Begin round robin reallocation");
		
		if(p2pDictionary == null) {
			
			throw new QueryPartAllocationException("No peer dictionary set");
			
		}
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID);
		peerIDs.removeAll(faultPeers);
		if(LOG.isDebugEnabled()) {
			
			logPeers(peerIDs);
			
		}
		
		if(peerIDs == null || peerIDs.isEmpty()) {
			
			throw new QueryPartAllocationException("There are no peers left to be considered for round robin in reallocation");
	
		}
		
		return roundRobinImpl(peerIDs);
		
	}
	
	/**
	 * The concrete round robin algorithm.
	 * @param peerIDs A list of all peers considered for allocation.
	 * @return The {@link PeerID} to allocate to.
	 */
	private static PeerID roundRobinImpl( 
			List<PeerID> peerIDs) {
		
		int peerIDIndex = 0;		
			
		PeerID chosenPeerID = null;
		
			if(peerIDs.size() > 0){
				
			chosenPeerID = peerIDs.get(peerIDIndex);
			}
			
			peerIDIndex = (peerIDIndex + 1) % peerIDs.size();
			
			LOG.debug("Allocated recovered query to peer {}", p2pDictionary.getRemotePeerName(chosenPeerID));
			
		
		
		LOG.debug("RoundRobinAllocation finished");
		return chosenPeerID;
		
	}

	/**
	 * Log the names of given peers.
	 * @param peerIDs The given peers.
	 */
	private static void logPeers(Collection<PeerID> peerIDs) {
		
		if(peerIDs == null || peerIDs.isEmpty()) {
			
			LOG.debug("\t<empty>");
			return;
			
		}
		
		for(PeerID peerID : peerIDs) {
			LOG.debug("\t{}", p2pDictionary.getRemotePeerName(peerID));
		}
	}

	/**
	 * Determine all peers, the round robin allocator can use.
	 * @param knownRemotePeers A collection of all known remote peers by ID.
	 * @param localPeerID The ID of the local peer.
	 * @return A list of {@link PeerID}s, the round robin allocator can use for allocation.
	 */
	protected abstract List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID);
	
}