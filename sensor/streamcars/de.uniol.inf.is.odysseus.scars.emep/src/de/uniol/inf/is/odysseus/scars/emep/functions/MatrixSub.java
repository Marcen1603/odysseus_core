package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixSub extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "MatrixSub";
	}

	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][]) getInputValue(0))
				.subtract(new RealMatrixImpl((double[][]) getInputValue(1))).getData();
	}

	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
