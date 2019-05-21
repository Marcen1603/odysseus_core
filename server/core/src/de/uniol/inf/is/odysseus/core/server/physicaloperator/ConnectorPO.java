package de.uniol.inf.is.odysseus.core.server.physicaloperator;

// Use MergePO as connector but provide another name
public class ConnectorPO extends MergePO {
	
	private final int port;
	
	public ConnectorPO(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

}
