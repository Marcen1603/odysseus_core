package de.uniol.inf.is.odysseus.dsp.sample.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "SAMPLESIGNAL", minInputPorts = 1, maxInputPorts = 1, doc = "todo", url = "todo", category = {
		LogicalOperatorCategory.PROCESSING })
public class SampleSignalAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -531044957735085573L;

	public SampleSignalAO() {
		super();
	}

	public SampleSignalAO(AbstractLogicalOperator abstractLogicalOperator) {
		super(abstractLogicalOperator);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SampleSignalAO(this);
	}

}
