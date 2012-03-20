package de.uniol.inf.is.odysseus.fusion.logicaloperator.filter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SPATIALFILTER")
public class SpatialFilterAO extends AbstractLogicalOperator  {
	
	private static final long serialVersionUID = 3958945913160253013L;

	public SpatialFilterAO() {
        super();
    }

    public SpatialFilterAO(final SpatialFilterAO ao) {
        super(ao);

    }

    @Override
    public SpatialFilterAO clone() {
        return new SpatialFilterAO(this);
    }

    @Override
    public SDFSchema getOutputSchema() {
        return this.getInputSchema(0);
    }

}

