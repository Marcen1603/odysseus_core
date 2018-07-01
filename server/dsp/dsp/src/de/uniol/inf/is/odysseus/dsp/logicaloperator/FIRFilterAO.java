package de.uniol.inf.is.odysseus.dsp.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "FIRFILTER", minInputPorts = 1, maxInputPorts = 1, doc = "todo", url = "todo", category = { LogicalOperatorCategory.PROCESSING })
public class FIRFilterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5633148709801986786L;

	public FIRFilterAO() {
		super();
	}

	public FIRFilterAO(FIRFilterAO firFilterAO) {
		super(firFilterAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FIRFilterAO(this);
	}

}
