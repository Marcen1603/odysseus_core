package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousMultiplicationOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1834647636880132250L;

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public String getSymbol() {
		return "*";
	}

	@Override
	public NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = this.getDistributions(((ProbabilisticContinuousDouble) this.getInputValue(0)).getDistribution());
		final NormalDistributionMixture b = this.getDistributions(((ProbabilisticContinuousDouble) this.getInputValue(1)).getDistribution());
		return getValueInternal(a, b);
	}

	protected NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
		final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();
		for (final Map.Entry<NormalDistribution, Double> aEntry : a.getMixtures().entrySet()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getKey().getMean());
			final RealMatrix aCovarianceMatrix = aEntry.getKey().getCovarianceMatrix().getMatrix();
			RealMatrix aInverseCovarianceMatrix;
			try {
				final CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(aCovarianceMatrix);
				final DecompositionSolver solver = choleskyDecomposition.getSolver();
				aInverseCovarianceMatrix = solver.getInverse();
			} catch (final Exception e) {
				final LUDecomposition luDecomposition = new LUDecomposition(aCovarianceMatrix);
				final DecompositionSolver solver = luDecomposition.getSolver();
				aInverseCovarianceMatrix = solver.getInverse();
			}

			for (final Map.Entry<NormalDistribution, Double> bEntry : b.getMixtures().entrySet()) {
				final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getKey().getMean());
				final RealMatrix bCovarianceMatrix = bEntry.getKey().getCovarianceMatrix().getMatrix();

				RealMatrix bInverseCovarianceMatrix;
				RealMatrix cCovarianceMatrix;

				try {
					final CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(bCovarianceMatrix);
					final DecompositionSolver solver = choleskyDecomposition.getSolver();
					bInverseCovarianceMatrix = solver.getInverse();
				} catch (final Exception e) {
					final LUDecomposition luDecomposition = new LUDecomposition(bCovarianceMatrix);
					final DecompositionSolver solver = luDecomposition.getSolver();
					bInverseCovarianceMatrix = solver.getInverse();
				}
				final RealMatrix aInversePlusBInverse = aInverseCovarianceMatrix.add(bInverseCovarianceMatrix);
				try {
					final CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(aInversePlusBInverse);
					final DecompositionSolver solver = choleskyDecomposition.getSolver();
					cCovarianceMatrix = solver.getInverse();
				} catch (final Exception e) {
					final LUDecomposition luDecomposition = new LUDecomposition(aInversePlusBInverse);
					final DecompositionSolver solver = luDecomposition.getSolver();
					cCovarianceMatrix = solver.getInverse();
				}
				final RealMatrix cMean = cCovarianceMatrix.multiply(aInverseCovarianceMatrix).multiply(aMean).add(cCovarianceMatrix.multiply(bInverseCovarianceMatrix).multiply(bMean));

				final NormalDistribution c = new NormalDistribution(cMean.getColumn(0), CovarianceMatrixUtils.fromMatrix(cCovarianceMatrix));
				mixtures.put(c, aEntry.getValue() * bEntry.getValue());
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
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	@Override
	public boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousMultiplicationOperator.accTypes;
	}

}
