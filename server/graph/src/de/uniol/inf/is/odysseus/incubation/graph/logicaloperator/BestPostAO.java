package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="BestPost", minInputPorts=1, maxInputPorts=1, doc="Calculate Post with most comments in graph", category={LogicalOperatorCategory.TRANSFORM})
public class BestPostAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 3713123005351765502L;

	public BestPostAO() {
		super();
	}
	
	public BestPostAO(BestPostAO other) {
		super(other);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new BestPostAO(this);
	}

}
