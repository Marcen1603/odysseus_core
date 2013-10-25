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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToProbabilisticContinuousDouble extends AbstractProbabilisticFunction<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5909577387865841893L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 2;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "toProbabilisticContinuousDouble";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final NormalDistributionMixture getValue() {
		final double[] means = ((double[][]) this.getInputValue(0))[0];
		final double[][] variances = (double[][]) this.getInputValue(1);

		final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		final MultivariateNormalDistribution component = new MultivariateNormalDistribution(means, variances);
		mvns.add(new Pair<Double, MultivariateNormalDistribution>(1.0, component));

		final NormalDistributionMixture result = new NormalDistributionMixture(mvns);
		final Interval[] support = new Interval[result.getSupport().length];
		for (int i = 0; i < result.getSupport().length; i++) {
			support[i] = Interval.MAX;
		}
		result.setSupport(support);
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

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE }, { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE } };

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
		return ToProbabilisticContinuousDouble.ACC_TYPES[argPos];
	}

}
