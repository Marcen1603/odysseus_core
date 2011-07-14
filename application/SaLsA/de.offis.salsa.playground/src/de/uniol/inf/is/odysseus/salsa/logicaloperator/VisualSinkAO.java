package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MAPSINK")
public class VisualSinkAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long serialVersionUID = -200760768896205654L;

    public VisualSinkAO() {
        super();
    }

    public VisualSinkAO(final VisualSinkAO ao) {
        super(ao);

    }

    @Override
    public VisualSinkAO clone() {
        return new VisualSinkAO(this);
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return this.getInputSchema(0);
    }

}
