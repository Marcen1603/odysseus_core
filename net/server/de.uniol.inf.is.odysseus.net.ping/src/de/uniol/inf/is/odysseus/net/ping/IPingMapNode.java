package de.uniol.inf.is.odysseus.net.ping;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IPingMapNode {

	public Vector3D getPosition();
	public IOdysseusNode getNode();

}