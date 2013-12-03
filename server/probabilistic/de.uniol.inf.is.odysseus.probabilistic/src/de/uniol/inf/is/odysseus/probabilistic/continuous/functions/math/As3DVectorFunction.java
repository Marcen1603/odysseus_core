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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class As3DVectorFunction extends AbstractProbabilisticFunction<NormalDistributionMixture[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8985768484486661113L;
	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE } };

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final String getSymbol() {
		return "as3DVector";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 3;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticIntegrateMultivariateFunction#getValue()
	 */
	@Override
	public final NormalDistributionMixture[] getValue() {
		return new NormalDistributionMixture[] { (NormalDistributionMixture) this.getInputValue(0), (NormalDistributionMixture) this.getInputValue(1), (NormalDistributionMixture) this.getInputValue(2) };
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math.ProbabilisticIntegrateMultivariateFunction#getAcceptedTypes(int)
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return As3DVectorFunction.ACC_TYPES[argPos];
	}

}
