package de.uniol.inf.is.odysseus.pubsub.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="Publish", minInputPorts=1, maxInputPorts=1, doc="Publish Operator")
public class Publish extends UnaryLogicalOp{
	
	public Publish(){
		super();
	}
	
	public Publish(Publish publish){
		super(publish);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new Publish(this);
	}

}
