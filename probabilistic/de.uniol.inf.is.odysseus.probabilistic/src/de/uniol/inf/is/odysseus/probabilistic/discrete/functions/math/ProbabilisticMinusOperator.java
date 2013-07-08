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
public class ProbabilisticMinusOperator extends AbstractProbabilisticBinaryOperator<ProbabilisticDouble> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -113226490599254958L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "-";
	}

	@Override
	public ProbabilisticDouble getValue() {
		final AbstractProbabilisticValue<?> a = this.getInputValue(0);
		final AbstractProbabilisticValue<?> b = this.getInputValue(1);
		return this.getValueInternal(a, b);
	}

	protected ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a, final AbstractProbabilisticValue<?> b) {
		final Map<Double, Double> values = new HashMap<Double, Double>(a.getValues().size() * b.getValues().size());
		for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
			for (final Entry<?, Double> bEntry : b.getValues().entrySet()) {
				final double value = ((Number) aEntry.getKey()).doubleValue() - ((Number) bEntry.getKey()).doubleValue();
				if (values.containsKey(value)) {
					values.put(value, values.get(value) + (aEntry.getValue() * bEntry.getValue()));
				} else {
					values.put(value, aEntry.getValue() * bEntry.getValue());
				}
			}
		}
		return new ProbabilisticDouble(values);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE;
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
	public boolean isLeftDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
		return false;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticMinusOperator.accTypes;
	}

}
