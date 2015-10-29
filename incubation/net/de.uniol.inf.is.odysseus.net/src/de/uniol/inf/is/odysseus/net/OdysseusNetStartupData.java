package de.uniol.inf.is.odysseus.net;

public final class OdysseusNetStartupData {

	private String nodeName;
	private OdysseusNodeID nodeID;
	private int communicationPort;

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

	public int getCommunicationPort() {
		return communicationPort;
	}

	public void setCommunicationPort(int communicationPort) {
		this.communicationPort = communicationPort;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{name=").append(nodeName).append(", comm-port=").append(communicationPort).append(", id=").append(nodeID).append("}");
		return sb.toString();
	}
}
