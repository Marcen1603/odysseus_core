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

package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticMultiplicationNumberRHSOperator extends ProbabilisticMultiplicationOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5137851422240905510L;

	@Override
	public ProbabilisticDouble getValue() {
		final AbstractProbabilisticValue<?> a = this.getInputValue(0);
		final ProbabilisticDouble b = new ProbabilisticDouble(this.getNumericalInputValue(1), 1.0);
		return this.getValueInternal(a, b);
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticMultiplicationNumberRHSOperator.accTypes[argPos];
	}

}
