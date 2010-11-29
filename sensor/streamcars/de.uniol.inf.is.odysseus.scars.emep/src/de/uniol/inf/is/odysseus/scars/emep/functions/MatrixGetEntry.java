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
		return ((double[][]) getInputValue(0))[(Integer)getInputValue(1)][(Integer)getInputValue(2)];
	}

	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
