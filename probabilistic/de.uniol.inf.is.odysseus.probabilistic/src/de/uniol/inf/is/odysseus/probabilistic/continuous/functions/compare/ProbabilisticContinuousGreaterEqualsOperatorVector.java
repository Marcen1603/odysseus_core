/*
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
import de.uniol.inf.is.odysseus.probabilistic.functions.ProbabilisticContinuousSelectUtils;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousGreaterEqualsOperatorVector extends AbstractProbabilisticBinaryOperator<ProbabilisticContinuousPredicateResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9122605635777338549L;

	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public String getSymbol() {
		return ">=";
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

		final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
		final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

		final double value = ProbabilisticContinuousSelectUtils.cumulativeProbability(mixtures, lowerBound, upperBound);
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
	public boolean isCommutative() {
		return false;
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

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
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
		return ProbabilisticContinuousGreaterEqualsOperatorVector.accTypes[argPos];
	}

}
