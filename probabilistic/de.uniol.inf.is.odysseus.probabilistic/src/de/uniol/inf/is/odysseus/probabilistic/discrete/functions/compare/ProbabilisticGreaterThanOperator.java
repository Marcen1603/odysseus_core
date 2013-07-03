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

package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticResult;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticGreaterThanOperator extends AbstractProbabilisticBinaryOperator<ProbabilisticResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3581873882761358906L;

	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public String getSymbol() {
		return ">";
	}

	@Override
	public ProbabilisticResult getValue() {
		AbstractProbabilisticValue<?> a = getInputValue(0);
		AbstractProbabilisticValue<?> b = getInputValue(1);
		ProbabilisticDouble result = new ProbabilisticDouble();
		double jointProbability = 0.0;
		for (Entry<?, Double> aEntry : a.getValues().entrySet()) {
			for (Entry<?, Double> bEntry : b.getValues().entrySet()) {
				if (((Number) aEntry.getKey()).doubleValue() > ((Number) bEntry.getKey()).doubleValue()) {
					double probability = aEntry.getValue() * bEntry.getValue();
					double key = ((Number) aEntry.getKey()).doubleValue();

					jointProbability += probability;
					if (!result.getValues().containsKey(key)) {
						result.getValues().put(key, probability);
					} else {
						result.getValues().put(key, result.getValues().get(key) + probability);
					}
				}
			}
		}
		return new ProbabilisticResult(jointProbability, result);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
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
	public boolean isLeftDistributiveWith(IOperator<ProbabilisticResult> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<ProbabilisticResult> operator) {
		return false;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		// accTypes[1] = String.class; // alphabetical order
		return accTypes;
	}

}
