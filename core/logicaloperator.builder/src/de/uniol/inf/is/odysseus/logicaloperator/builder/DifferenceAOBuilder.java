package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

public class DifferenceAOBuilder extends AbstractOperatorBuilder {

	public DifferenceAOBuilder() {
		super(2,2);
	}
	
	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new DifferenceAO();
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
