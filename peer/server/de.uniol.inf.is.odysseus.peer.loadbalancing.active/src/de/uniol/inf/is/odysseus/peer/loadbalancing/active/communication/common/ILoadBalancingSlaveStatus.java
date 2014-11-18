package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.peer.PeerID;

/**
 * Interface for a Master-Peer-Status Class for LoadBalancing Defines only
 * common needed Methods, e.g. to get the current CommunicationStrategy.
 * 
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingSlaveStatus {

	/***
	 * Returns current LoadBalancing-Process Id
	 * 
	 * @return
	 */
	public int getLbProcessId();

	/***
	 * Returns PeerID of Master Peer.
	 * 
	 * @return
	 */
	public PeerID getMasterPeer();

	/***
	 * Returns name of Communication Strategy
	 * 
	 * @return
	 */
	public String getCommunicatorName();
}
