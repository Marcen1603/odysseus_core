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
	 * Assigns query parts to peers, where they shall be executed. <br />
	 * This method respects set destinations via {@link QueryPart#QueryPart(Collection, String)}.
	 * @param remotePeerIDs A collection of all available peers.
	 * @param queryParts 
	 * @return A collection of query parts to be assigned.
	 */
	public Map<QueryPart, PeerID> assignQueryPartsToPeers(Collection<PeerID> remotePeerIDs, Collection<QueryPart> queryParts);
	
	/**
	 * Assigns query parts to peers, where they shall be executed. <br />
	 * This method does not respects set destinations via {@link QueryPart#QueryPart(Collection, String)}.
	 * @param remotePeerIDs A collection of all available peers.
	 * @param queryParts 
	 * @return A collection of query parts to be assigned.
	 */
	public Map<QueryPart, PeerID> assignQueryPartsToPeersIgnoreSetDestinations(Collection<PeerID> remotePeerIDs, Collection<QueryPart> queryParts);

}