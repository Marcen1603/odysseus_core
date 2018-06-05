package de.uniol.inf.is.odysseus.net.ping.impl;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.ping.IPingMapNode;

public final class PingMapNode implements IPingMapNode {

	private final IOdysseusNode node;
	private Vector3D position = new Vector3D(0,0,0);

	PingMapNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "odysseus node for being a pingmapnode must not be null!");
		
		this.node = node;
	}

	@Override
	public Vector3D getPosition() {
		return new Vector3D(position.getX(), position.getY(), position.getZ());
	}
	
	@Override
	public IOdysseusNode getNode() {
		return node;
	}

	public void setPosition(Vector3D position) {
		Preconditions.checkNotNull(position, "Position for ping map node must not be null!");
		
		this.position = position;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(node);
		sb.append(":");
		sb.append(toString(position));
		return sb.toString();
	}
	
	private static String toString(Vector3D v) {
		return v.getX() + " / " + v.getY() + " / " + v.getZ();
	}

}
