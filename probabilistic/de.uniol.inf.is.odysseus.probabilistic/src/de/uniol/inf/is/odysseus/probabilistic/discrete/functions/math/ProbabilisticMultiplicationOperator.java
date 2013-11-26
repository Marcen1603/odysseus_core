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
import java.util.Objects;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticMultiplicationOperator extends AbstractProbabilisticBinaryOperator<ProbabilisticDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1356177009585423741L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 5;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "*";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public ProbabilisticDouble getValue() {
		final AbstractProbabilisticValue<?> a = this.getInputValue(0);
		final AbstractProbabilisticValue<?> b = this.getInputValue(1);
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return this.getValueInternal(a, b);
	}

	/**
	 * Multiplies the given probabilistic value with the other probabilistic value.
	 * 
	 * @param a
	 *            The probabilistic value
	 * @param b
	 *            The probabilistic value
	 * @return The probabilistic value a*b
	 */
	protected final ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a, final AbstractProbabilisticValue<?> b) {
		final Map<Double, Double> values = new HashMap<Double, Double>(a.getValues().size() * b.getValues().size());
		for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
			for (final Entry<?, Double> bEntry : b.getValues().entrySet()) {
				final double value = ((Number) aEntry.getKey()).doubleValue() * ((Number) bEntry.getKey()).doubleValue();
				if (values.containsKey(value)) {
					values.put(value, values.get(value) + (aEntry.getValue() * bEntry.getValue()));
				} else {
					values.put(value, aEntry.getValue() * bEntry.getValue());
				}
			}
		}
		return new ProbabilisticDouble(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IOperator#getAssociativity()
	 */
	@Override
	public final de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isCommutative()
	 */
	@Override
	public boolean isCommutative() {
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isAssociative()
	 */
	@Override
	public final boolean isAssociative() {
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isLeftDistributiveWith(de.uniol.inf.is.odysseus.mep.IOperator)
	 */
	@Override
	public final boolean isLeftDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IBinaryOperator#isRightDistributiveWith(de.uniol.inf.is.odysseus.mep.IOperator)
	 */
	@Override
	public final boolean isRightDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
		return (operator.getClass() == ProbabilisticPlusOperator.class) || (operator.getClass() == ProbabilisticMinusOperator.class) || (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
	}

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticMultiplicationOperator.ACC_TYPES;
	}

}
