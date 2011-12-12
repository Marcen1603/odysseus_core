package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "VGRIDSINK")
public class VisualGridSinkAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long serialVersionUID = -200760768896205654L;

    public VisualGridSinkAO() {
        super();
    }

    public VisualGridSinkAO(final VisualGridSinkAO ao) {
        super(ao);

    }

    @Override
    public VisualGridSinkAO clone() {
        return new VisualGridSinkAO(this);
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return this.getInputSchema(0);
    }

}
