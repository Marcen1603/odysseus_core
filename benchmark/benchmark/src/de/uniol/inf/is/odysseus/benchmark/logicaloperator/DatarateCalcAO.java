package de.uniol.inf.is.odysseus.benchmark.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(name = "DatarateCalc", minInputPorts = 1, maxInputPorts = 1)
public class DatarateCalcAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1912158762824502004L;

	long updateRate = 100;
	
	public DatarateCalcAO() {
	}
	
	public DatarateCalcAO(DatarateCalcAO other){
		super(other);
		this.updateRate = other.updateRate;
	}
	
	public long getUpdateRate() {
		return updateRate;
	}
	
	@Parameter(type = IntegerParameter.class)
	public void setUpdateRate(long updateRate) {
		this.updateRate = updateRate;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DatarateCalcAO(this);
	}

}
