package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;


public class QDLSubscribableWithPort {
	private final int port;
	private final IQDLOperator<?> op;
	
	public QDLSubscribableWithPort(int port,IQDLOperator<?> op) {
		this.port = port;
		this.op = op;
	}
	
	public QDLSubscribableWithPort(IQDLOperator<?> op,int port) {
		this.port = port;
		this.op = op;
	}

	public int getPort() {
		return port;
	}

	public IQDLOperator<?> getOp() {
		return op;
	}

	
	
	
}
