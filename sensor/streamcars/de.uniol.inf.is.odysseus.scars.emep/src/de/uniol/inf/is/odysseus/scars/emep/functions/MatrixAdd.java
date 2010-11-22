package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MatrixAdd extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "MatrixAdd";
	}

	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][]) getInputValue(0)).add(new RealMatrixImpl((double[][]) getInputValue(1)));
	}

	@Override
	public Class<? extends Object> getType() {
		return RealMatrix.class;
	}

}
