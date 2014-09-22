package de.uniol.inf.is.odysseus.peer.ping;

import org.apache.commons.math.geometry.Vector3D;

import net.jxta.peer.PeerID;

public interface IPingMapNode {

	public Vector3D getPosition();
	public PeerID getPeerID();

}