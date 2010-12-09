package de.uniol.inf.is.odysseus.latency.pql;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationAO;

public class LatencyCalculatorAOBuilder extends AbstractOperatorBuilder {

	public LatencyCalculatorAOBuilder(int minPortCount, int maxPortCount) {
		super(1, Integer.MAX_VALUE);
	}

	private static final long serialVersionUID = 617674702618645795L;

	@Override
	protected boolean internalValidation() {
		// TODO: Kann man pruefen ob Latenzen verwendet werden??
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new LatencyCalculationAO();
	}

}
