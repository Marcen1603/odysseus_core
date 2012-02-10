package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

@LogicalOperator(name = "SOCKETSINK", minInputPorts = 1, maxInputPorts = 1)
public class SocketSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4250341797170265988L;

	private Integer sinkPort;
	private String sinkType;

	private Boolean loginNeeded = false;
	private String sinkName;
	
	public SocketSinkAO(int sinkPort, String sinkType, boolean loginNeeded, String name){
		this.sinkPort = sinkPort;
		this.sinkType = sinkType;
		this.loginNeeded = loginNeeded;
		this.sinkName = name;
	}
	
	public SocketSinkAO(){
	}
	
	public SocketSinkAO(SocketSinkAO socketSinkAO) {
		super(socketSinkAO);
		this.sinkPort = socketSinkAO.sinkPort;
		this.sinkType = socketSinkAO.sinkType;
		this.loginNeeded = socketSinkAO.loginNeeded;
		this.sinkName = socketSinkAO.sinkName;
	}

	@Parameter(name="SINKPORT",type = IntegerParameter.class, optional = false)
	public void setSinkPort(int sinkPort){
		this.sinkPort = sinkPort;
	}
	
	public int getSinkPort() {
		return sinkPort;
	}
	
	public String getSinkType(){
		return sinkType;
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SocketSinkAO(this);
	}

	public boolean isLoginNeeded() {
		return loginNeeded;
	}
	
	public String getSinkName() {
		return sinkName;
	}
	
	@Parameter(name = "SINKNAME", type = StringParameter.class, optional = true)
	public void setSinkName(String sinkName){
		this.sinkName = sinkName;
	}

	@Parameter(name = "SINKTYPE", type = StringParameter.class, optional = false)
	public void setSinkType(String sinkType) {
		this.sinkType = sinkType;
	}
	
	@Parameter(name="LOGINNEEDED", type = BooleanParameter.class, optional = true)
	public void setLoginNeeded(boolean loginNeeded) {
		this.loginNeeded = loginNeeded;
	}
}
