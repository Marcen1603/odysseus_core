package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;


@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RPiGPIOSink", doc="Sink for Raspberry Pi GPIO-Port", category={LogicalOperatorCategory.SINK})
public class RPiGPIOSinkAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = 1L;
	
	public RPiGPIOSinkAO() {
		super();
		
		
	}
	
	public RPiGPIOSinkAO(RPiGPIOSinkAO rpiGPIOSinkAO) {
		super(rpiGPIOSinkAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new RPiGPIOSinkAO(this);
	}

}
