package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@LogicalOperator(name = "SOCKETSINK", minInputPorts = 1, maxInputPorts = 1)
public class SocketSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4250341797170265988L;

	final int sinkPort;
	final String sinkType;

	final private boolean loginNeeded;
	
	public SocketSinkAO(int sinkPort, String sinkType, boolean loginNeeded){
		this.sinkPort = sinkPort;
		this.sinkType = sinkType;
		this.loginNeeded = loginNeeded;
	}
	
	public SocketSinkAO(SocketSinkAO socketSinkAO) {
		super(socketSinkAO);
		this.sinkPort = socketSinkAO.sinkPort;
		this.sinkType = socketSinkAO.sinkType;
		this.loginNeeded = socketSinkAO.loginNeeded;
	}

	public int getSinkPort() {
		return sinkPort;
	}
	
	public String getSinkType(){
		return sinkType;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SocketSinkAO(this);
	}

	public boolean isLoginNeeded() {
		return loginNeeded;
	}

}
