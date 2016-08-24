package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "STDataStructure", doc = "Fills a spatio temporal data structure", category = {
		LogicalOperatorCategory.BASE })
public class STDataStructureAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6941816005065513833L;

	public STDataStructureAO() {
		super();
	}

	public STDataStructureAO(STDataStructureAO ao) {
		super(ao);
	}

	@Override
	public STDataStructureAO clone() {
		return new STDataStructureAO(this);
	}

}
