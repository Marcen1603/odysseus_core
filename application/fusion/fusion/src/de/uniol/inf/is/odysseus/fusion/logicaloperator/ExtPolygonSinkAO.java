package de.uniol.inf.is.odysseus.fusion.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ExtPolygonSink")
public class ExtPolygonSinkAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = 3958945913160253013L;

	public ExtPolygonSinkAO() {
        super();
    }

    public ExtPolygonSinkAO(final ExtPolygonSinkAO ao) {
        super(ao);

    }

    @Override
    public ExtPolygonSinkAO clone() {
        return new ExtPolygonSinkAO(this);
    }

    @Override
    public SDFSchema getOutputSchema() {
        return this.getInputSchema(0);
    }


}

