package de.uniol.inf.is.odysseus.datarate.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator( name = "Datarate", doc = "Calculates the datarate and inserts the results into metadata", minInputPorts = 1, maxInputPorts = 1, category = {LogicalOperatorCategory.BENCHMARK}, hidden = false)
public class DatarateAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private int updateRateElements;
	private String key;

	public DatarateAO() {

	}

	public DatarateAO( DatarateAO other ) {
		super(other);

		this.updateRateElements = other.updateRateElements;
		this.key = other.key;
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

	@Parameter(type = StringParameter.class, optional = false, doc = "Name of the measure point")
	public void setKey(String key) {
		this.key = key;
		addParameterInfo("KEY", key+"");
	}

	public String getKey() {
		return key;
	}

}
