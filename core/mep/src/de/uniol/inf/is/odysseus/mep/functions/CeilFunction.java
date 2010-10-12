package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class CeilFunction extends AbstractFunction<Double> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "ceil";
	}

	@Override
	public Double getValue() {
		return Math.ceil(getNumericalInputValue(0));
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}
