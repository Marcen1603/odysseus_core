package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts=Integer.MAX_VALUE, minInputPorts=1, name="Sink")
public class SinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5432522510944805110L;
	
	public SinkAO(AbstractLogicalOperator op) {
		super(op);
	}

	public SinkAO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SinkAO(this);
	}

}
