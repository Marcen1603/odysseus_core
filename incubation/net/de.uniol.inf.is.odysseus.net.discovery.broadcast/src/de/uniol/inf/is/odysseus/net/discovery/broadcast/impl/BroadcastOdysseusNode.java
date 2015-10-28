package de.uniol.inf.is.odysseus.net.discovery.broadcast.impl;

import java.net.InetAddress;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

public class BroadcastOdysseusNode implements IOdysseusNode {

	private final OdysseusNodeID nodeID;
	private final String nodeName;
	private final InetAddress address;
	
	public BroadcastOdysseusNode(OdysseusNodeID nodeID, String name, InetAddress address) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");
		Preconditions.checkNotNull(address, "address must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Nodename must not be null or empty!");

		this.nodeID = nodeID;
		this.nodeName = name;
		this.address = address;
	}
	
	@Override
	public OdysseusNodeID getID() {
		return nodeID;
	}

	@Override
	public String getName() {
		return nodeName;
	}

	@Override
	public InetAddress getInetAddress() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof BroadcastOdysseusNode )) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		
		BroadcastOdysseusNode other = (BroadcastOdysseusNode)obj;
		return this.nodeID.equals(other.nodeID);
	}
	
	@Override
	public int hashCode() {
		return this.nodeID.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Node ").append(nodeID).append(": ").append(nodeName).append(" [").append(address).append("]}");
		return sb.toString();
	}
}
