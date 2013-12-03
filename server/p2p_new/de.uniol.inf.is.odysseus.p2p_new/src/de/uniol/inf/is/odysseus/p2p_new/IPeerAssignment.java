package de.uniol.inf.is.odysseus.p2p_new;

import java.util.Collection;
import java.util.Map;


import net.jxta.peer.PeerID;

/**
 * The interface for peer assignment strategies.
 * @author Michael Brand
 */
public interface IPeerAssignment {
	
	/**
	 * Returns the name of the peer assignment strategy.
	 */
	public String getName();
	
	/**
	 * Assigns query parts to peers, where they shall be executed.
	 * @param remotePeerIDs A collection of all available peers.
	 * @param queryParts A collection of query parts to be assigned.
	 * @return A mapping of assigned peer IDs to the query parts.
	 */
	public Map<QueryPart, PeerID> assignQueryPartsToPeers(Collection<PeerID> remotePeerIDs, 
			Collection<QueryPart> queryParts);

}