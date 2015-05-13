package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * 
 * @author Henrik Surm
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SETSYSTEMTIME", doc = "The SetSystemTime operator sets the system time to the timestamp of incoming elements when the difference is too big. ", category = { LogicalOperatorCategory.BASE })
public class SetSystemTimeAO extends UnaryLogicalOp {

    private static final long serialVersionUID = -1992998023364461468L;
    
    private int threshold=50;

    public SetSystemTimeAO() {
        super();
    }

    public SetSystemTimeAO(SetSystemTimeAO other) {
        super(other);
    }

    @Parameter(type = IntegerParameter.class, name = "threshold", isList = false, optional = true, doc = "Max allowed difference between system time and element time stamp before system time is set")
	public void setThreshold(int threshold) 
	{
		this.threshold = threshold;
	}    
	
	public int getThreshold()
	{
		return threshold;
	}
    
    @Override
    public AbstractLogicalOperator clone() {
        return new SetSystemTimeAO(this);
    }
}
