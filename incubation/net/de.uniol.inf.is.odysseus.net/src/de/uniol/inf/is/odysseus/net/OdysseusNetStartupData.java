package de.uniol.inf.is.odysseus.net;

public final class OdysseusNetStartupData {

	private String nodeName;
	private OdysseusNodeID nodeID;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public OdysseusNodeID getNodeID() {
		return nodeID;
	}

	public void setNodeID(OdysseusNodeID nodeID) {
		this.nodeID = nodeID;
	}
}
