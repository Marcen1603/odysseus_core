package de.uniol.inf.is.odysseus.net.ping;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IPingMap {

	public Optional<Double> getPing(IOdysseusNode toNode);
	public Optional<Double> getRemotePing( IOdysseusNode startNode, IOdysseusNode endNode);

	public Optional<IPingMapNode> getPingNode(IOdysseusNode peer);
	public Vector3D getLocalPosition();
	public void setPosition(IOdysseusNode node, Vector3D position);

	public void addListener( IPingMapListener listener );
	public void removeListener( IPingMapListener listener );
	
}
