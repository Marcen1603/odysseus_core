package de.uniol.inf.is.odysseus.mep.impl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;

public class MatrixLine extends AbstractFunction<double[]> {

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
	public double[] getValue() {
		int arity = getArity();
		double[] value = new double[arity];
		for (int i = 0; i < arity; ++i) {
			value[i] = (Double) getArgument(i).getValue();
		}
		return value;
	}

	@Override
	public Class<? extends double[]> getType() {
		return double[].class;
	}

}
