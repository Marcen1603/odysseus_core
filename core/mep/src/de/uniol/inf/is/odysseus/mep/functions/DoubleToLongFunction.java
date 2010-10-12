package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class DoubleToLongFunction extends AbstractFunction<Long> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "doubleToLong";
	}

	@Override
	public Long getValue() {
		return getNumericalInputValue(0).longValue();
	}

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

}
