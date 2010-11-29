package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixGetEntry extends AbstractFunction<Object>{

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "MatrixEntry";
	}

	@Override
	public Object getValue() {
		return ((double[][]) getInputValue(0))[((Double)getInputValue(1)).intValue()][((Double)getInputValue(2)).intValue()];
	}

	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
