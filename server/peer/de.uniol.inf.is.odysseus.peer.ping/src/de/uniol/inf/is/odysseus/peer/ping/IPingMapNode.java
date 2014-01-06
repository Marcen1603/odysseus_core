package de.uniol.inf.is.odysseus.peer.ping;

import net.jxta.peer.PeerID;

public interface IPingMapNode {

	public abstract double getX();

	public abstract double getY();

	public abstract PeerID getPeerID();

}