package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.base.predicate.ProbabilisticContinuousPredicateResult;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for continuous probabilistic values
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousEqualsOperatorVector extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
     * 
     */
	private static final long serialVersionUID = 3016679134461973157L;

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public String getSymbol() {
		return "==";
	}

	@Override
	public NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = ((NormalDistributionMixture) this.getInputValue(0)).clone();

		final double[][] b = (double[][]) this.getInputValue(1);
		final double[] lowerBoundData = new double[a.getDimension()];
		Arrays.fill(lowerBoundData, Double.NEGATIVE_INFINITY);
		System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);
		final double[] upperBoundData = new double[a.getDimension()];
		Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);
		System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);

		final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
		final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

		a.setScale(Double.POSITIVE_INFINITY);
		final Interval[] support = new Interval[a.getDimension()];
		for (int i = 0; i < a.getDimension(); i++) {
			Interval interval = new Interval(lowerBound.getEntry(i), upperBound.getEntry(i));
			support[i] = a.getSupport(i).intersection(interval);
		}
		a.setSupport(support);
		return a;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT,
					SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG }, { SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE } };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousEqualsOperatorVector.ACC_TYPES[argPos];
	}
}
