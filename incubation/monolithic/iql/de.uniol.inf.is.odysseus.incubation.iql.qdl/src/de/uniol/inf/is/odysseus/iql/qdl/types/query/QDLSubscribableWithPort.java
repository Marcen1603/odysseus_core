package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class QDLSubscribableWithPort {
	private final int port;
	private final ILogicalOperator op;
	
	public QDLSubscribableWithPort(int port,ILogicalOperator op) {
		this.port = port;
		this.op = op;
	}
	
	public QDLSubscribableWithPort(ILogicalOperator op,int port) {
		this.port = port;
		this.op = op;
	}

	public int getPort() {
		return port;
	}

	public ILogicalOperator getOp() {
		return op;
	}

	
	
	
}
