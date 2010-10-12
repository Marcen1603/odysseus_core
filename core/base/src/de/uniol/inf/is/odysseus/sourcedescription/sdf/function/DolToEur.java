package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class DolToEur extends AbstractFunction<Double> {

	private static double EXCHANGERATE = 1d / 1.55d;

	@Override
	public String getSymbol() {
		return "DolToEur";
	}

	public static void setExchangeRate(double value) {
		EXCHANGERATE = value;
	}

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public Double getValue() {
		double value = ((Number) getInputValue(0)).doubleValue();
		value *= EXCHANGERATE;
		return value;
	}

	@Override
	public Class<? extends Double> getType() {
		return Double.class;
	}

}
