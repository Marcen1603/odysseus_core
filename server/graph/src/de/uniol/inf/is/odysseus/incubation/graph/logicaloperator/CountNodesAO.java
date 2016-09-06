package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="CountNodes", minInputPorts=1, maxInputPorts=1, doc="Count nodes in a graph", category={LogicalOperatorCategory.TRANSFORM})
public class CountNodesAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6991939720638719339L;

	public CountNodesAO() {
		super();
	}
	
	public CountNodesAO(CountNodesAO countNodesAo) {
		super(countNodesAo);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CountNodesAO(this);
	}

}
