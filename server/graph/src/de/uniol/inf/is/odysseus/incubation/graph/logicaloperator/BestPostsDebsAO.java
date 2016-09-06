package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="BestPostsDebs", minInputPorts=1, maxInputPorts=1, doc="Calculate Best Posts for Query1 Debs Challenge", category={LogicalOperatorCategory.TRANSFORM})
public class BestPostsDebsAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -3522190343381081084L;

	public BestPostsDebsAO() {
		super();
	}
	
	public BestPostsDebsAO(BestPostsDebsAO other) {
		super(other);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new BestPostsDebsAO(this);
	}

}
