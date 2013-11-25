package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;

public interface IP2PNetworkManager {
	
	public void setLocalPeerName( String name );
	public String getLocalPeerName();
	public void setLocalPeerGroupName( String name );
	public String getLocalPeerGroupName();
	
	public void start();
	public void stop();
	
	public PeerID getLocalPeerID();
	public PeerGroup getLocalPeerGroup();
	public PeerGroupID getLocalPeerGroupID();
}
