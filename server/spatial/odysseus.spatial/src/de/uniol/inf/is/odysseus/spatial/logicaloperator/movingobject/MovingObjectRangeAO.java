package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MOVINGOBJECTRANGE", doc = "Puts out all objects in the given range around the given moving object in the given data structure.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MovingObjectRangeAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -1956793493229036126L;

	public MovingObjectRangeAO() {
		super();
	}

	public MovingObjectRangeAO(MovingObjectRangeAO ao) {

	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MovingObjectRangeAO(this);
	}

}
