package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TimeSplitAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = 7650066151075034323L;

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeSplitAO();
	}

}
