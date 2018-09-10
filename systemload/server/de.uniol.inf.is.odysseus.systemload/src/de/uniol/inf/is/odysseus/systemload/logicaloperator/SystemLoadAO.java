package de.uniol.inf.is.odysseus.systemload.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="SystemLoad", maxInputPorts=1, minInputPorts=1, category = { LogicalOperatorCategory.BENCHMARK }, doc="This operator set the current system load information to the meta data of this operator.")
public class SystemLoadAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4912875344976409173L;
	private String loadName = "local";

	public SystemLoadAO(){
		
	}
	
	public SystemLoadAO(SystemLoadAO systemLoadAO) {
		super(systemLoadAO);
		this.loadName = systemLoadAO.loadName;
	}

	@Override
	public SystemLoadAO clone() {
		return new SystemLoadAO(this);
	}
	
	@Parameter(name="LoadName", type=StringParameter.class, optional=true, doc="The name for this system value set")
	public void setLoadName(String loadName) {
		this.loadName = loadName;
	}

	public String getLoadName() {
		return loadName;
	}

}
