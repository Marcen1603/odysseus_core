package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixTranspose extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "MatrixTrans";
	}

	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][]) getInputValue(0)).transpose().getData();
	}

	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
