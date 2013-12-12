package de.uniol.inf.is.odysseus.peer.resource;

import net.jxta.peer.PeerID;

public interface IPeerResourceUsageManagerListener {

	public void resourceUsageChanged( IPeerResourceUsageManager sender, PeerID peerID );
}
