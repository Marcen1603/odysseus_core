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

package de.uniol.inf.is.odysseus.probabilistic.function;

import org.apache.commons.math3.util.*;
import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousGreaterEqualsOperatorVector extends
		AbstractProbabilisticBinaryOperator<Double> {

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
	public Double getValue() {
		ProbabilisticContinuousDouble a = getInputValue(0);
		RealVector lowerBound = MatrixUtils.createRealVector(((double[][]) this
				.getInputValue(1))[0]);
		double[] upperBoundData = new double[lowerBound.getDimension()];
		Arrays.fill(upperBoundData, Double.NEGATIVE_INFINITY);
		RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);
		NormalDistributionMixture mixtures = getDistributions(a
				.getDistribution());
		double value = ProbabilisticContinuousSelectUtils
				.cumulativeProbability(mixtures, lowerBound, upperBound);
		mixtures.setScale(mixtures.getScale() * value);
		Interval[] support = new Interval[lowerBound.getDimension()];
		for (int i = 0; i < support.length; i++) {
			double lower = FastMath.max(mixtures.getSupport(i).inf(),
					lowerBound.getEntry(i));
			double upper = FastMath.min(mixtures.getSupport(i).sup(),
					upperBound.getEntry(i));
			support[i] = new Interval(lower, upper);
		}
		mixtures.setSupport(support);
		return value;
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
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
					SDFProbabilisticDatatype.PROBABILISTIC_LONG },
			{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT,
					SDFDatatype.VECTOR_DOUBLE } };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return accTypes[argPos];
	}

}
