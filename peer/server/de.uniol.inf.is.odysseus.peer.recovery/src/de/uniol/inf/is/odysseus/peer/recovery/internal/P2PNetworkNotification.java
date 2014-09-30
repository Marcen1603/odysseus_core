package de.uniol.inf.is.odysseus.peer.recovery.internal;

import net.jxta.peer.PeerID;

public class P2PNetworkNotification {
	
	public final static String LOST_PEER = "lost";
	public final static String FOUND_PEER = "found";
	
	private String type;
	private PeerID peer;

	public P2PNetworkNotification(String type, PeerID peer) {
		super();
		this.type = type;
		this.peer = peer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PeerID getPeer() {
		return peer;
	}

	public void setPeer(PeerID peer) {
		this.peer = peer;
	}

}
