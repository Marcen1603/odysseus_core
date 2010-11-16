package de.uniol.inf.is.odysseus.mep.impl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;

/**
 * Internal function for building arrays.
 * Expects Double[] as arguments and builds a matrix out of them.
 * No check for rectangularity is applied. This is checked in the ExpressionBuilderVisitor!
 */
public class MatrixFunction extends AbstractFunction<Double[][]> {
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
	public Double[][] getValue() {
		int arity = getArity();
		Double[][] value = new Double[arity][];
		for(int i = 0; i < arity; ++i){
			value[i] = (Double[]) getArgument(i).getValue();
		}
		
		return value;
	}

	@Override
	public Class<? extends Double[][]> getType() {
		return Double[][].class;
	}

}
