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

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticIntegrateMultivariateFunction extends AbstractIntegrateMultivariateFunction {

	/**
     * 
     */
	private static final long serialVersionUID = 144107943090837242L;
	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
		{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE }, 
		{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE } };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final Double getValue() {
		final ProbabilisticContinuousDouble continuousDouble = (ProbabilisticContinuousDouble) this.getInputValue(0);
		final RealVector lowerBound = MatrixUtils.createRealVector(((double[][]) this.getInputValue(1))[0]);
		final RealVector upperBound = MatrixUtils.createRealVector(((double[][]) this.getInputValue(2))[0]);
		return this.getValueInternal(continuousDouble, lowerBound, upperBound);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument: A distribution and the lower and upper support.");
		}
		return ProbabilisticIntegrateMultivariateFunction.ACC_TYPES[argPos];
	}

}
