package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@LogicalOperator(name = "SOCKETSINK", minInputPorts = 1, maxInputPorts = 1)
public class SocketSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4250341797170265988L;

	final int sinkPort;
	
	public SocketSinkAO(int sinkPort){
		this.sinkPort = sinkPort;
	}
	
	public SocketSinkAO(SocketSinkAO socketSinkAO) {
		this.sinkPort = socketSinkAO.sinkPort;
	}

	public int getSinkPort() {
		return sinkPort;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SocketSinkAO(this);
	}

}
