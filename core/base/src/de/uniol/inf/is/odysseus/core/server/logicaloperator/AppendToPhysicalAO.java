package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This Operator can be used to append a subquery to an existing 
 * phyiscal plan
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="AppendTo")
public class AppendToPhysicalAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6717408016417095953L;

	String appendTo = null;
	
	public AppendToPhysicalAO() {
		super();
	}
	
	public AppendToPhysicalAO(AppendToPhysicalAO appendToPhysicalAO) {
		super(appendToPhysicalAO);
		this.appendTo = appendToPhysicalAO.appendTo;
	}

	@Parameter(type = StringParameter.class, name = "appendTo", optional=false)
	public void setAppendTo(String appendTo){
		this.appendTo = appendTo;
	}
	
	public String getAppendTo() {
		return appendTo;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new AppendToPhysicalAO(this);
	}

}
