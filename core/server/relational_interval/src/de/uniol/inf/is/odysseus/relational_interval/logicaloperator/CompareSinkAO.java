package de.uniol.inf.is.odysseus.relational_interval.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="Compare", maxInputPorts=2, minInputPorts=2, doc="Compares to input streams", category={ LogicalOperatorCategory.TEST})
public class CompareSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1694209133347516252L;

	public CompareSinkAO(CompareSinkAO op) {
		super(op);
	}

	public CompareSinkAO() {

	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CompareSinkAO(this);
	}

}
