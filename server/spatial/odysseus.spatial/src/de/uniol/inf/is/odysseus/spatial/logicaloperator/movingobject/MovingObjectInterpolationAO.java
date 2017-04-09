package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MovingObjectInterpolation", doc = "Interpolates the location of moving objects.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MovingObjectInterpolationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1235279982306004707L;

	public MovingObjectInterpolationAO() {
		super();
	}
	
	public MovingObjectInterpolationAO(MovingObjectInterpolationAO ao) {
		super(ao);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MovingObjectInterpolationAO(this);
	}

}
