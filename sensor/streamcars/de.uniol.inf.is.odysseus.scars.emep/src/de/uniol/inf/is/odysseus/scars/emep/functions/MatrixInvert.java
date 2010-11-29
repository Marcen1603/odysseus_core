package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixInvert extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "MatrixInv";
	}

	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][])getInputValue(0)).inverse().getData();
	}

	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
