package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "VPOLYGONSINK")
public class VisualPolygonSinkAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long serialVersionUID = -200760768896205654L;

    public VisualPolygonSinkAO() {
        super();
    }

    public VisualPolygonSinkAO(final VisualPolygonSinkAO ao) {
        super(ao);

    }

    @Override
    public VisualPolygonSinkAO clone() {
        return new VisualPolygonSinkAO(this);
    }

    @Override
    public SDFSchema getOutputSchema() {
        return this.getInputSchema(0);
    }

}
