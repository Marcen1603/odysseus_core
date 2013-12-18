package de.uniol.inf.is.odysseus.test.sinks.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

public class CompareSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4955868217997286974L;

	public CompareSinkAO() {
	
	}
	
	public CompareSinkAO(CompareSinkAO compareSinkAO) {
		super(compareSinkAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CompareSinkAO(this);		
	}

}
