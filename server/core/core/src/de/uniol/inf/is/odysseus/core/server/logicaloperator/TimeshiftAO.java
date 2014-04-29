package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

@LogicalOperator(name="TIMESHIFT", maxInputPorts=1, minInputPorts=1, doc="Shifts the timestamp(s) a given time", category = {LogicalOperatorCategory.PROCESSING})
public class TimeshiftAO extends AbstractLogicalOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5266611795908459618L;
	private long shift = 0L;
	
	public TimeshiftAO(){
		
	}
	
	public TimeshiftAO(TimeshiftAO timeshiftAO) {
		this.shift = timeshiftAO.shift;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeshiftAO(this);
	}

	public long getShift() {
		return shift;
	}

	@Parameter(name="Shift", type=LongParameter.class)
	public void setShift(long shift) {
		this.shift = shift;
	}

}
