package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.List;

import net.jxta.peer.PeerID;

/**
 * This interface describes the methods used for initiating communication between the initiating peer and the volunteering peer in active load balancing.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingCommunicator {
	/**
	 * Inititates LoadBalancing between Peer and the volunteering (other) Peer
	 * @param otherPeer volunteering Peer which can take query Part.
	 * @param queryId query Id of Query which should be removed to volunteering Peer.
	 */
	public void initiateLoadBalancing(PeerID otherPeer, int queryId);
	
	/**
	 * Registers a LoadBalancing Listener, which is notified when LoadBalancing is finished.
	 * @param listener Listener to register.
	 */
	public void registerLoadBalancingListener(ILoadBalancingListener listener);
	
	/**
	 * Removes a LoadBalancing Listener.
	 * @param listener Listener to remove.
	 */
	public void removeLoadBalancingListener(ILoadBalancingListener listener);
	
	/**
	 * Returns name of specific Communicator
	 * @return Name of Communcator
	 */
	public String getName();
	
	
	/**
	 * Returns list of Peers involved in LoadBalancing. (Usually neighbors).
	 * @param query
	 * @return
	 */
	public List<PeerID> getInvolvedPeers(int queryID);
}
