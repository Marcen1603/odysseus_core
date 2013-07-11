package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticContinuousPlusOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533914833718506956L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	@Override
	public NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = (NormalDistributionMixture) this.getInputValue(0);
		final NormalDistributionMixture b = (NormalDistributionMixture) this.getInputValue(1);
		return getValueInternal(a, b);
	}

	protected NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
		final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();
		for (final Map.Entry<NormalDistribution, Double> aEntry : a.getMixtures().entrySet()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getKey().getMean());
			final RealMatrix aCovarianceMatrix = aEntry.getKey().getCovarianceMatrix().getMatrix();

			for (final Map.Entry<NormalDistribution, Double> bEntry : b.getMixtures().entrySet()) {
				final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getKey().getMean());
				final RealMatrix bCovarianceMatrix = bEntry.getKey().getCovarianceMatrix().getMatrix();

				NormalDistribution distribution = new NormalDistribution(aMean.add(bMean).getColumn(0), CovarianceMatrixUtils.fromMatrix(aCovarianceMatrix.add(bCovarianceMatrix)));
				mixtures.put(distribution, aEntry.getValue() * bEntry.getValue());
			}
		}
		return new NormalDistributionMixture(mixtures);
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
		return false;
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

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousPlusOperator.accTypes;
	}

}
