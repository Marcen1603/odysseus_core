package de.uniol.inf.is.odysseus.fusion.logicaloperator.metadata;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "UpdateIFPMetadata")
public class UpdataFusionMetadataAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = 3958945913160253013L;

	public UpdataFusionMetadataAO() {
        super();
    }

    public UpdataFusionMetadataAO(final UpdataFusionMetadataAO ao) {
        super(ao);
    }

    @Override
    public UpdataFusionMetadataAO clone() {
        return new UpdataFusionMetadataAO(this);
    }

//    @Override
//    public SDFSchema getOutputSchema() {
//        return this.getInputSchema(0);
//    }


}

