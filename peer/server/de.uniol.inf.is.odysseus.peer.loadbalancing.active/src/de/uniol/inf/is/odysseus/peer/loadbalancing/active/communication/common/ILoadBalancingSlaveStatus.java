package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.peer.PeerID;

public interface ILoadBalancingSlaveStatus {

	/***
	 * Returns current LoadBalancing-Process Id
	 * @return
	 */
	public int getLbProcessId();

	/***
	 * Returns PeerID of Master Peer.
	 * @return
	 */
	public PeerID getMasterPeer();
	
	/***
	 * Returns name of Communication Strategy
	 * @return
	 */
	public String getCommunicatorName();
}
