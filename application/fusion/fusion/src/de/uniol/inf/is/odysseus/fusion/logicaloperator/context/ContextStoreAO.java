package de.uniol.inf.is.odysseus.fusion.logicaloperator.context;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "FusionContextStore")
public class ContextStoreAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = 3958945913160253013L;

	public ContextStoreAO() {
        super();
    }

    public ContextStoreAO(final ContextStoreAO ao) {
        super(ao);
    }

    @Override
    public ContextStoreAO clone() {
        return new ContextStoreAO(this);
    }

    @Override
    public SDFSchema getOutputSchema() {
        return this.getInputSchema(0);
    }


}

