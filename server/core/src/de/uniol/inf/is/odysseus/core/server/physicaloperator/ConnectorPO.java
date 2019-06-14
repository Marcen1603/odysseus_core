package de.uniol.inf.is.odysseus.core.server.physicaloperator;

/**
 * This physical operator can be used to connect other operators in an already defined query. As an addition to MergePO this operator
 * has a port number. It is typically used in SubQueryPO to define, which inputs of the SubQueryPO relate to which input in the subplan
 * 
 * @author Marco Grawunder
 *
 */
public class ConnectorPO extends MergePO {
	
	private final int port;
	
	public ConnectorPO(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

}
