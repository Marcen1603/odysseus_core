package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public interface ILoadBalancingQueryManager {
	
	public void sendRegisterAsSlave(PeerID masterPeer, ID sharedQueryID);
	
	public void sendUnregisterAsSlave(PeerID masterPeer, ID sharedQueryID);
	
	public void sendChangeMaster(PeerID slavePeer, ID sharedQueryID);
	
	public void sendChangeMasterToAllSlaves(ID sharedQueryID);
	
}
