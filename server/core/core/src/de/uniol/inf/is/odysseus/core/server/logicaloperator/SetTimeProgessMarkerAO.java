package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SetTimeProgressMarker", doc = "This operator updates the time order marker flag for each tuple. It can be used to state that an input stream should not be used to determine time progess.", category = { LogicalOperatorCategory.PROCESSING })
public class SetTimeProgessMarkerAO extends UnaryLogicalOp  {

	private static final long serialVersionUID = -1241822989806999613L;
	
	boolean value;
	
	public SetTimeProgessMarkerAO() {
	
	}
	
	public SetTimeProgessMarkerAO(SetTimeProgessMarkerAO timeOrderMarker) {
		super(timeOrderMarker);
	}

	public boolean getValue() {
		return value;
	}

	@Parameter(optional=false, type=BooleanParameter.class)
	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SetTimeProgessMarkerAO(this);
	}

}
