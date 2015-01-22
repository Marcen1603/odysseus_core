package de.uniol.inf.is.odysseus.p2p_new;

import java.net.InetAddress;

import net.jxta.peer.PeerID;

public class PeerReachabilityInfo {

	private final PeerID peerID;
	private final String peerName;
	private final String peerGroupName;
	private final InetAddress address;
	private final int jxtaPort;
	
	public PeerReachabilityInfo(PeerID peerID, String peerName, InetAddress address, int jxtaPort, String peerGroupName) {
		this.peerName = peerName;
		this.peerID = peerID;
		this.address = address;
		this.jxtaPort = jxtaPort;
		this.peerGroupName = peerGroupName;
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
}
