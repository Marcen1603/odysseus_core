package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RandomFunction extends AbstractFunction<Double> {

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public String getSymbol() {
		return "rnd";
	}

	@Override
	public Double getValue() {
		return Math.random();
	}

	@Override
	public Class<? extends Double> getType() {
		return Double.class;
	}

}
