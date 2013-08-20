package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="SyncWithSystemTime", doc="This operator tries to delay elements so that they are not faster than realtime.")
public class SyncWithSystemTimeAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1271244329291011562L;
	private TimeUnit timeUnit = null;
	private double factor = -1;
 
	public SyncWithSystemTimeAO() {
	}	
		
	public SyncWithSystemTimeAO(SyncWithSystemTimeAO syncWithSystemTimeAO) {
		super(syncWithSystemTimeAO);
		this.timeUnit = syncWithSystemTimeAO.timeUnit;
		this.factor = syncWithSystemTimeAO.factor;
	}

	@Parameter(type = StringParameter.class, name = "ApplicationTimeUnit", optional=true, doc="Unit of application timestamps")
	public void setApplicationTimeUnit(String timeUnit) {
		this.timeUnit = TimeUnit.valueOf(timeUnit);
	}

	@Parameter(type = DoubleParameter.class, name = "ApplicationTimeFactor", optional=true, doc="Factor to calculate milliseconds from application time")
	public void setApplicationTimeFactor(double factor){
		this.factor = factor;
	}
	
	public double getFactor() {
		return factor;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SyncWithSystemTimeAO(this);
	}

	@Override
	public boolean isValid() {
		if (factor == -1 && timeUnit == null){
			factor = 1.0;
		}
		if (factor < 0){
			addError(new IllegalParameterException(
					"ApplicationTimeFactor must be greater 0")); 
			return false;			
		}
		if (factor != -1 && timeUnit != null){
			addError(new IllegalParameterException(
					"can't use ApplicationTimeFactor and ApplicationTimeUnit at same time")); 
			return false;
		}
		
		return true;
	}
	
}
