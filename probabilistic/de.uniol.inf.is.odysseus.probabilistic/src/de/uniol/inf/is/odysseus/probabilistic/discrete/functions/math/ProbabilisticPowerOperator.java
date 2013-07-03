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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticPowerOperator extends
		AbstractProbabilisticBinaryOperator<ProbabilisticDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4646698914179052402L;

	@Override
	public int getPrecedence() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "^";
	}

	@Override
	public ProbabilisticDouble getValue() {
		AbstractProbabilisticValue<?> a = getInputValue(0);
		double b = getNumericalInputValue(1);
		return getValueInternal(a, b);
	}

	protected ProbabilisticDouble getValueInternal(
			AbstractProbabilisticValue<?> a, double b) {
		Map<Double, Double> values = new HashMap<Double, Double>(a.getValues()
				.size());
		for (Entry<?, Double> aEntry : a.getValues().entrySet()) {
			double value = FastMath.pow(
					((Number) aEntry.getKey()).doubleValue(), b);
			// Does the value already exists in the map, i.e., a^0
			if (values.containsKey(value)) {
				values.put(value, values.get(value) + aEntry.getValue());
			} else {
				values.put(value, aEntry.getValue());
			}
		}
		return new ProbabilisticDouble(values);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE;
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
	public boolean isLeftDistributiveWith(
			IOperator<ProbabilisticDouble> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(
			IOperator<ProbabilisticDouble> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
					SDFProbabilisticDatatype.PROBABILISTIC_LONG },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

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
