package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class SQIMVN extends AbstractFunction<Double> {
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
			{ SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX },
			{ SDFDatatype.VECTOR_DOUBLE }, { SDFDatatype.VECTOR_DOUBLE } };

	@Override
	public String getSymbol() {
		return "SQIMVN";
	}

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

}
