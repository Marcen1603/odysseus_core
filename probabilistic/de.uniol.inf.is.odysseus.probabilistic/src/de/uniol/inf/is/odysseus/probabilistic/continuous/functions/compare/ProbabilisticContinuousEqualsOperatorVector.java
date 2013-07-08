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
public class ProbabilisticContinuousEqualsOperatorVector extends AbstractProbabilisticBinaryOperator<ProbabilisticContinuousPredicateResult> {

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
	public ProbabilisticContinuousPredicateResult getValue() {
		final ProbabilisticContinuousDouble a = this.getInputValue(0);
		final NormalDistributionMixture mixtures = this.getDistributions(a.getDistribution());

		final double[][] b = (double[][]) this.getInputValue(1);
		final double[] lowerBoundData = new double[mixtures.getDimension()];
		Arrays.fill(lowerBoundData, Double.NEGATIVE_INFINITY);
		System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);
		final double[] upperBoundData = new double[mixtures.getDimension()];
		Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);
		System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);

		final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
		final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

		final double value = 0.0;
		mixtures.setScale(mixtures.getScale() * value);
		final Interval[] support = new Interval[mixtures.getDimension()];
		for (int i = 0; i < mixtures.getDimension(); i++) {
			final double lower = FastMath.max(mixtures.getSupport(i).inf(), lowerBound.getEntry(i));
			final double upper = FastMath.min(mixtures.getSupport(i).sup(), upperBound.getEntry(i));
			support[i] = new Interval(lower, upper);
		}
		mixtures.setSupport(support);
		return new ProbabilisticContinuousPredicateResult(value, mixtures);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
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
	public boolean isLeftDistributiveWith(final IOperator<ProbabilisticContinuousPredicateResult> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(final IOperator<ProbabilisticContinuousPredicateResult> operator) {
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
