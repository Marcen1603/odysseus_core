package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "ConsoleSink", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE, doc = "Print input to standard out.", category = { LogicalOperatorCategory.SINK })
public class ConsoleSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -4314825703308226640L;

	public ConsoleSinkAO() {
		super();
	}

	public ConsoleSinkAO(ConsoleSinkAO sink) {
		super(sink);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConsoleSinkAO(this);
	}

}
