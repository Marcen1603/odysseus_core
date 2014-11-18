package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.peer.PeerID;

/**
 * Interface for a Master-Peer-Status Class for LoadBalancing Defines only
 * common needed Methods, e.g. to get the current CommunicationStrategy.
 * 
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingMasterStatus {

	/***
	 * Returns name of communication Strategy.
	 * 
	 * @return
	 */
	public String getCommunicationStrategy();

	/**
	 * Returns (peer-unique) LoadBalancing process-id.
	 * 
	 * @return
	 */
	public int getProcessId();

	/**
	 * Sets (peer-unique) LoadBalancing process-id
	 * 
	 * @param processId
	 */
	public void setProcessId(int processId);

	/**
	 * Returns id of logical Query
	 * 
	 * @return
	 */
	public int getLogicalQuery();

	/**
	 * Sets id of logical query.
	 * 
	 * @param logicalQuery
	 */
	public void setLogicalQuery(int logicalQuery);

	/***
	 * Returns Peer ID of volunteering Peer.
	 * 
	 * @return
	 */
	public PeerID getVolunteeringPeer();

	/**
	 * Sets Peer ID of volunteering Peer.
	 * 
	 * @param volunteeringPeer
	 */
	public void setVolunteeringPeer(PeerID volunteeringPeer);

}
