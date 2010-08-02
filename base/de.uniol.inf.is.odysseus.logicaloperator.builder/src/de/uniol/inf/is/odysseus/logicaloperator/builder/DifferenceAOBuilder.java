package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;

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
