package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RPiGPIOAccess", doc="Source for Raspberry Pi GPIO-Port", category={LogicalOperatorCategory.SOURCE})
public class RPiGPIOAccessAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = 1L;
	
	public RPiGPIOAccessAO() {
		super();
		
		
	}
	
	public RPiGPIOAccessAO(RPiGPIOAccessAO rpiGPIOAccessAO) {
		super(rpiGPIOAccessAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new RPiGPIOAccessAO(this);
	}

}
