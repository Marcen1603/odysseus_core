package de.uniol.inf.is.odysseus.datarate.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator( name = "Datarate", doc = "Calculates the datarate and inserts the results into metadata", minInputPorts = 1, maxInputPorts = 1, category = {LogicalOperatorCategory.BENCHMARK}, hidden = false)
public class DatarateAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private int updateRateElements;
	
	public DatarateAO() {
		
	}
	
	public DatarateAO( DatarateAO other ) {
		super(other);
		
		this.updateRateElements = other.updateRateElements;
	}
	
	@Override
	public DatarateAO clone() {
		return new DatarateAO(this);
	}
	
	@Parameter(name = "UpdateRate", doc = "Element count after recalculating the datarate. Zero means no measurements.", type = IntegerParameter.class, optional = false)
	public void setUpdateRate( int updateRate ) {
		this.updateRateElements = updateRate;
		addParameterInfo("UPDATERATE", String.valueOf(updateRate));
	}
	
	public int getUpdateRate() {
		return updateRateElements;
	}
}
