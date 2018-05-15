package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "TIMEWINDOW", category = { LogicalOperatorCategory.BASE }, doc = "The window sets the validity of the tuple. The default time granularity is in milliseconds. So, if you have another time granularity, you may use the unit-parameter (e.g. use 5 for size and SECONDS for the unit parameter) or you have to adjust the arity (e.g. use 5000 for size without the unit parameter)", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/TimeWindow", hidden = true)
public class TimeWindowAO extends AbstractWindowWithWidthAO {

	private static final long serialVersionUID = 6219339401064522226L;

	public TimeWindowAO() {
		super(WindowType.TIME);
	}

	public TimeWindowAO(AbstractWindowAO windowPO) {
		super(windowPO);
	}

	@Override
	public TimeWindowAO clone() {
		return new TimeWindowAO(this);
	}


}
