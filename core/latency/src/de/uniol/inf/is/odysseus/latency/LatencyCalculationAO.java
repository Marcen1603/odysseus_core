package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LatencyCalculationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -9078812899082643674L;
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LatencyCalculationAO();
	}

}
