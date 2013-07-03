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
		NormalDistributionMixture a = getDistributions(((ProbabilisticContinuousDouble) getInputValue(0)).getDistribution());
		NormalDistributionMixture b = getDistributions(((ProbabilisticContinuousDouble) getInputValue(1)).getDistribution());
		// return getValueInternal(a, b);
		throw new RuntimeException("Operator (" + getSymbol() + ") not implemented");
	}

	protected NormalDistributionMixture getValueInternal(NormalDistributionMixture a, NormalDistributionMixture b) {
		Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();
		for (Map.Entry<NormalDistribution, Double> aEntry : a.getMixtures().entrySet()) {
			RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getKey().getMean());
			RealMatrix aCovarianceMatrix = aEntry.getKey().getCovarianceMatrix().getMatrix();
			RealMatrix aInverseCovarianceMatrix;
			try {
				CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(aCovarianceMatrix);
				DecompositionSolver solver = choleskyDecomposition.getSolver();
				aInverseCovarianceMatrix = solver.getInverse();
			} catch (Exception e) {
				LUDecomposition luDecomposition = new LUDecomposition(aCovarianceMatrix);
				DecompositionSolver solver = luDecomposition.getSolver();
				aInverseCovarianceMatrix = solver.getInverse();
			}

			for (Map.Entry<NormalDistribution, Double> bEntry : b.getMixtures().entrySet()) {
				RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getKey().getMean());
				RealMatrix bCovarianceMatrix = bEntry.getKey().getCovarianceMatrix().getMatrix();

				RealMatrix bInverseCovarianceMatrix;
				RealMatrix cCovarianceMatrix;

				try {
					CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(bCovarianceMatrix);
					DecompositionSolver solver = choleskyDecomposition.getSolver();
					bInverseCovarianceMatrix = solver.getInverse();
				} catch (Exception e) {
					LUDecomposition luDecomposition = new LUDecomposition(bCovarianceMatrix);
					DecompositionSolver solver = luDecomposition.getSolver();
					bInverseCovarianceMatrix = solver.getInverse();
				}
				RealMatrix aInversePlusBInverse = aInverseCovarianceMatrix.add(bInverseCovarianceMatrix);
				try {
					CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(aInversePlusBInverse);
					DecompositionSolver solver = choleskyDecomposition.getSolver();
					cCovarianceMatrix = solver.getInverse();
				} catch (Exception e) {
					LUDecomposition luDecomposition = new LUDecomposition(aInversePlusBInverse);
					DecompositionSolver solver = luDecomposition.getSolver();
					cCovarianceMatrix = solver.getInverse();
				}
				RealMatrix cMean = cCovarianceMatrix.multiply(aInverseCovarianceMatrix).multiply(aMean).add(cCovarianceMatrix.multiply(bInverseCovarianceMatrix).multiply(bMean));

				NormalDistribution c = new NormalDistribution(cMean.getColumn(0), CovarianceMatrixUtils.fromMatrix(cCovarianceMatrix));
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
	public boolean isLeftDistributiveWith(IOperator<NormalDistributionMixture> operator) {
		return operator.getClass() == ProbabilisticPlusOperator.class || operator.getClass() == ProbabilisticMinusOperator.class || operator.getClass() == PlusOperator.class || operator.getClass() == MinusOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<NormalDistributionMixture> operator) {
		return operator.getClass() == ProbabilisticPlusOperator.class || operator.getClass() == ProbabilisticMinusOperator.class || operator.getClass() == PlusOperator.class || operator.getClass() == MinusOperator.class;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return accTypes;
	}

}
