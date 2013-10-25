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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.ProbabilisticContinuousUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Greater-Equals operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousGreaterEqualsOperatorVector extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9122605635777338549L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 8;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return ">=";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final NormalDistributionMixture getValue() {
		final Object[] aVector = this.getInputValue(0);
		final NormalDistributionMixture a = ((NormalDistributionMixture) aVector[0]).clone();

		final double[][] b = (double[][]) this.getInputValue(1);
		final double[] lowerBoundData = new double[a.getDimension()];
		Arrays.fill(lowerBoundData, Double.NEGATIVE_INFINITY);
		System.arraycopy(b[0], 0, lowerBoundData, 0, b[0].length);
		final double[] upperBoundData = new double[a.getDimension()];
		Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);

		final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
		final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

		final double value = ProbabilisticContinuousUtils.cumulativeProbability(a, lowerBound, upperBound);
		a.setScale(a.getScale() / value);
		final Interval[] support = new Interval[a.getDimension()];
		for (int i = 0; i < a.getDimension(); i++) {
			final Interval interval = new Interval(lowerBound.getEntry(i), upperBound.getEntry(i));
			support[i] = a.getSupport(i).intersection(interval);
		}
		a.setSupport(support);
		return a;
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

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getAssociativity()
	 */
	@Override
	public final de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE } };

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
		return ProbabilisticContinuousGreaterEqualsOperatorVector.ACC_TYPES[argPos];
	}

}
