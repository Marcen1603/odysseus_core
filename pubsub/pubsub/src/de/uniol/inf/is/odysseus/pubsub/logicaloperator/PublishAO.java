package de.uniol.inf.is.odysseus.pubsub.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="Publish", minInputPorts=1, maxInputPorts=1, doc="Publish Operator")
public class PublishAO extends UnaryLogicalOp{
	
	private String topologyType;
	
	public PublishAO(){
		super();
	}
	
	public PublishAO(PublishAO publish){
		super(publish);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PublishAO(this);
	}
	
	@Parameter(name="topologyType", type=StringParameter.class)
	public void setTopologyType(String topologyType){
		this.topologyType = topologyType;
	}
	
	public String getTopologyType(){
		return topologyType;
	}

}
