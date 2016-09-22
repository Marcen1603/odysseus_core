package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name="GraphToTuples", doc="Converts a tuple containing a graph element to tuples representing the graph", category = {LogicalOperatorCategory.TRANSFORM})
public class GraphToTuplesAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4170687295586712499L;

	public GraphToTuplesAO() {
	}
	
	public GraphToTuplesAO(GraphToTuplesAO graphToTuples) {
		super(graphToTuples);
	}
	
	@Override
	public GraphToTuplesAO clone() {
		return new GraphToTuplesAO(this);
	}

}
