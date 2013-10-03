package de.uniol.inf.is.odysseus.p2p_new.distribute.peerAssignment;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignment;
import de.uniol.inf.is.odysseus.p2p_new.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;

import net.jxta.peer.PeerID;

/**
 * An peer assignment strategy, which assigns peers to query parts via round-robin.
 * @author Michael Brand
 */
public class RRPeerAssignment implements IPeerAssignment {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RRPeerAssignment.class);
	
	/**
	 * The number of the next peer to be assigned (round-robin).
	 */
	private static int peerCounter = 0;
	
	/**
	 * @see #getName()
	 */
	private static final String NAME = "round-robin";

	@Override
	public String getName() {
		
		return NAME;
		
	}

	@Override
	public Map<QueryPart, PeerID> assignQueryPartsToPeers(Collection<PeerID> remotePeerIDs, 
			Collection<QueryPart> queryParts) {
		
		Preconditions.checkNotNull(remotePeerIDs);
		Preconditions.checkArgument(remotePeerIDs.size() > 0);
		Preconditions.checkNotNull(queryParts);
		
		// The return value
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		
		// The iterator for the query parts.
		final Iterator<QueryPart> partsIter = queryParts.iterator();
		
		while(partsIter.hasNext()) {
			
			// The current query part
			QueryPart part = partsIter.next();
			
			// The name of the assigned peer if present
			Optional<String> peerName;
			
			// The ID of the assigned peer (Round-Robin)
			PeerID peerID = ((List<PeerID>) remotePeerIDs).get(peerCounter);
			peerCounter = (++peerCounter) % remotePeerIDs.size();
			
			distributed.put(part, peerID);
			
			peerName = DistributionHelper.getPeerName(peerID);
			if(peerName.isPresent())
				LOG.debug("Assign query part {} to peer {}", part, peerName.get());
			else LOG.debug("Assign query part {} to peer {}", part, peerID);
			
		}

		return distributed;
		
	}

}
