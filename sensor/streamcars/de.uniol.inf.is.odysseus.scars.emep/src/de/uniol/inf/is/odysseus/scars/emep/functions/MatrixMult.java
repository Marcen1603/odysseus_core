package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixMult extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "MatrixMult";
	}

	@Override
	public Object getValue() {
		if (getInputValue(1) instanceof double[][]) {
			return new RealMatrixImpl((double[][]) getInputValue(0))
					.multiply(new RealMatrixImpl((double[][]) getInputValue(1)));
		} else {
			return new RealMatrixImpl((double[][]) getInputValue(0))
					.scalarMultiply((Double) getInputValue(1));
		}
	}

	@Override
	public Class<? extends Object> getType() {
		return RealMatrix.class;
	}

}
