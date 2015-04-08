package de.uniol.inf.is.odysseus.peer.broadcast;

import java.net.InetAddress;

import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

public class PeerReachabilityInfo {

	private final PeerID peerID;
	private final String peerName;
	private final String peerGroupName;
	private final PeerGroupID peerGroupID;
	private final InetAddress address;
	private final int jxtaPort;
	
	public PeerReachabilityInfo(PeerID peerID, String peerName, InetAddress address, int jxtaPort, String peerGroupName, PeerGroupID peerGroupID) {
		this.peerName = peerName;
		this.peerID = peerID;
		this.address = address;
		this.jxtaPort = jxtaPort;
		this.peerGroupName = peerGroupName;
		this.peerGroupID = peerGroupID;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getJxtaPort() {
		return jxtaPort;
	}
	
	public PeerID getPeerID() {
		return peerID;
	}
	
	public String getPeerName() {
		return peerName;
	}
	
	public String getPeerGroupName() {
		return peerGroupName;
	}
	
	public PeerGroupID getPeerGroupID() {
		return peerGroupID;
	}
}
