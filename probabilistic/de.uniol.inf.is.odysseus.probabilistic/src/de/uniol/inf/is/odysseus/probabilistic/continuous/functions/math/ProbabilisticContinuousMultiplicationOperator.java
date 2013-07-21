/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 5;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "*";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = (NormalDistributionMixture) this.getInputValue(0);
		final NormalDistributionMixture b = (NormalDistributionMixture) this.getInputValue(1);
		return this.getValueInternal(a, b);
	}

	/**
	 * Multiplies the given distribution with the other distribution.
	 * 
	 * @param a
	 *            The distribution
	 * @param b
	 *            The distribution to multiply
	 * @return The distribution of a*b
	 */
	protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
		final List<Pair<Double, MultivariateNormalDistribution>> mixtures = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		for (final Pair<Double, MultivariateNormalDistribution> aEntry : a.getMixtures().getComponents()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMeans());
			final RealMatrix aCovarianceMatrix = aEntry.getValue().getCovariances();
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

			for (final Pair<Double, MultivariateNormalDistribution> bEntry : b.getMixtures().getComponents()) {
				final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMeans());
				final RealMatrix bCovarianceMatrix = bEntry.getValue().getCovariances();

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

				final MultivariateNormalDistribution c = new MultivariateNormalDistribution(cMean.getColumn(0), cCovarianceMatrix.getData());
				mixtures.add(new Pair<Double, MultivariateNormalDistribution>(aEntry.getKey() * bEntry.getKey(), c));
			}
		}

		final NormalDistributionMixture result = new NormalDistributionMixture(mixtures);
		final Interval[] support = new Interval[a.getSupport().length];
		for (int i = 0; i < a.getSupport().length; i++) {
			support[i] = a.getSupport(i).mul(b.getSupport(i));
		}
		result.setSupport(support);
		result.setScale(a.getScale() * b.getScale());
		return result;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getAssociativity()
	 */
	@Override
	public final de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isCommutative()
	 */
	@Override
	public final boolean isCommutative() {
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isAssociative()
	 */
	@Override
	public final boolean isAssociative() {
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isLeftDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isRightDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousMultiplicationOperator.ACC_TYPES;
	}

}
