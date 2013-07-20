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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousPlusOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533914833718506956L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 6;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "+";
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
	 * Adds the given distribution to the other distribution.
	 * 
	 * @param a
	 *            The distribution
	 * @param b
	 *            The distribution to add
	 * @return The distribution of a+b
	 */
	protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
		final Map<MultivariateNormalDistribution, Double> mixtures = new HashMap<MultivariateNormalDistribution, Double>();
		for (final Map.Entry<MultivariateNormalDistribution, Double> aEntry : a.getMixtures().entrySet()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getKey().getMeans());
			final RealMatrix aCovarianceMatrix = aEntry.getKey().getCovariances();

			for (final Map.Entry<MultivariateNormalDistribution, Double> bEntry : b.getMixtures().entrySet()) {
				final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getKey().getMeans());
				final RealMatrix bCovarianceMatrix = bEntry.getKey().getCovariances();

				final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(aMean.add(bMean).getColumn(0), aCovarianceMatrix.add(bCovarianceMatrix).getData());
				mixtures.put(distribution, aEntry.getValue() * bEntry.getValue());
			}
		}

		final NormalDistributionMixture result = new NormalDistributionMixture(mixtures);
		final Interval[] support = new Interval[a.getSupport().length];
		for (int i = 0; i < a.getSupport().length; i++) {
			support[i] = a.getSupport(i).add(b.getSupport(i));
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
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isAssociative()
	 */
	@Override
	public final boolean isAssociative() {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isLeftDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isRightDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
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
		return ProbabilisticContinuousPlusOperator.ACC_TYPES;
	}

}
