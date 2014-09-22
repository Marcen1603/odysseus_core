package de.uniol.inf.is.odysseus.p2p_new;

import java.net.URI;

import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;

public interface IP2PNetworkManager {
	
	public void setLocalPeerName( String name );
	public String getLocalPeerName();
	public void setLocalPeerGroupName( String name );
	public String getLocalPeerGroupName();
	public void setRendevousPeer( boolean isRendevousPeer );
	public boolean isRendevousPeer();
	public void setRendevousPeerAddress(URI peerAddress);
	public URI getRendevousPeerAddress();
	public void setPort( int port );
	public int getPort();
	
	public void start() throws P2PNetworkException;
	public void stop();
	public boolean isStarted();
	
	public PeerID getLocalPeerID();
	public PeerGroup getLocalPeerGroup();
	public PeerGroupID getLocalPeerGroupID();
	
	public void addListener( IP2PNetworkListener listener );
	public void removeListener( IP2PNetworkListener listener );
	public void addAdvertisementListener( IAdvertisementDiscovererListener listener);
	public void removeAdvertisementListener( IAdvertisementDiscovererListener listener);

}
