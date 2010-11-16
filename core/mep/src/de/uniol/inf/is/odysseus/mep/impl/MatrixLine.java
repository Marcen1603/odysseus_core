package de.uniol.inf.is.odysseus.mep.impl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;

public class MatrixLine extends AbstractFunction<Double[]> {

	private int arity;
	
	public MatrixLine(IExpression<?>[] values) {
		this.arity = values.length;
		setArguments(values);
	}

	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public String getSymbol() {
		return "__matrixline";
	}

	@Override
	public Double[] getValue() {
		int arity = getArity();
		Double[] value = new Double[arity];
		for (int i = 0; i < arity; ++i) {
			value[i] = (Double) getArgument(i).getValue();
		}
		return value;
	}

	@Override
	public Class<? extends Double[]> getType() {
		return Double[].class;
	}

}
