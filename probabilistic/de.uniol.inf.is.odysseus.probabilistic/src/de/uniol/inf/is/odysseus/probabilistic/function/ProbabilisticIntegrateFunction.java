package de.uniol.inf.is.odysseus.probabilistic.function;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticIntegrateFunction extends
		ProbabilisticIntegrateMultivariateFunction {

	/**
     * 
     */
	private static final long serialVersionUID = 144107943090837242L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

	@Override
	public Double getValue() {
		ProbabilisticContinuousDouble continuousDouble = (ProbabilisticContinuousDouble) this
				.getInputValue(0);
		RealVector lowerBound = MatrixUtils
				.createRealVector(new double[] { getNumericalInputValue(1) });
		RealVector upperBound = MatrixUtils
				.createRealVector(new double[] { getNumericalInputValue(2) });
		return getValueInternal(continuousDouble, lowerBound, upperBound);
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument: A distribution, a covariance matrix and the lower and upper support.");
		}
		return accTypes[argPos];
	}

}
