package de.uniol.inf.is.odysseus.fusion.logicaloperator.association;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "SPATIALASSOCIATION")
public class SpatialAssociationAO extends AbstractLogicalOperator  {
	
	private static final long serialVersionUID = 3958945913160253013L;

	public SpatialAssociationAO() {
        super();
    }

    public SpatialAssociationAO(final SpatialAssociationAO ao) {
        super(ao);

    }

    @Override
    public SpatialAssociationAO clone() {
        return new SpatialAssociationAO(this);
    }

}

