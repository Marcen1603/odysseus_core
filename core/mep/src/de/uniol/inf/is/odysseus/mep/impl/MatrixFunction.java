package de.uniol.inf.is.odysseus.mep.impl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;

/**
 * Internal function for building arrays.
 * Expects Double[] as arguments and builds a matrix out of them.
 * No check for rectangularity is applied. This is checked in the ExpressionBuilderVisitor!
 */
public class MatrixFunction extends AbstractFunction<double[][]> {
	private int arity;

	public MatrixFunction(IExpression<?>[] lines) {
		this.arity = lines.length;
		setArguments(lines);
	}
	
	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public String getSymbol() {
		return "__matrix";
	}

	@Override
	public double[][] getValue() {
		int arity = getArity();
		double[][] value = new double[arity][];
		for(int i = 0; i < arity; ++i){
			value[i] = (double[]) getArgument(i).getValue();
		}
		
		return value;
	}

	@Override
	public Class<? extends double[][]> getType() {
		return double[][].class;
	}

}
