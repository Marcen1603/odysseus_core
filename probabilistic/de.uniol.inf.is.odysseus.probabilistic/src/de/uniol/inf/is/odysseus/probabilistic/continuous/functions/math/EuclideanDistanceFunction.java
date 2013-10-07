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
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EuclideanDistanceFunction extends AbstractEuclideanDistanceFunction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4967226185023253568L;
	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument: Two distribution.");
		}
		return EuclideanDistanceFunction.ACC_TYPES[argPos];
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final Double getValue() {
		final RealMatrix a = MatrixUtils.createColumnRealMatrix(new double[]  { this.getNumericalInputValue(0)  });
		final RealMatrix b = MatrixUtils.createColumnRealMatrix(new double[] { this.getNumericalInputValue(1)  });
		return this.getValueInternal(a, b);
	}
}
