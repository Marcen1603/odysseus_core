package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Logical operator for GraphIntersection.
 * 
 * @author Kristian Bruns
 */
@LogicalOperator(name="GraphIntersection", minInputPorts=1, maxInputPorts=1, doc="Calculate intersection of all graphs", category={LogicalOperatorCategory.TRANSFORM})
public class GraphIntersectionAO extends UnaryLogicalOp {
	
	private static final long serialVersionUID = 7983693721481085829L;

	public GraphIntersectionAO() {
		super();
	}
	
	public GraphIntersectionAO(GraphIntersectionAO other) {
		super(other);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new GraphIntersectionAO(this);
	}
}
