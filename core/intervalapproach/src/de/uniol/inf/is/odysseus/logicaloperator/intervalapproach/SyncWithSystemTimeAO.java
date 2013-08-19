package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="SyncWithSystemTime", doc="This operator tries to delay elements so that they are not faster than realtime.")
public class SyncWithSystemTimeAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1271244329291011562L;
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

	public SyncWithSystemTimeAO() {
	}	
		
	public SyncWithSystemTimeAO(SyncWithSystemTimeAO syncWithSystemTimeAO) {
		super(syncWithSystemTimeAO);
		this.timeUnit = syncWithSystemTimeAO.timeUnit;
	}

	@Parameter(type = StringParameter.class, name = "ApplicationTimeUnit", optional=true, doc="Unit of application timestamps")
	public void setApplicationTimeUnit(String timeUnit) {
		this.timeUnit = TimeUnit.valueOf(timeUnit);
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SyncWithSystemTimeAO(this);
	}

}
