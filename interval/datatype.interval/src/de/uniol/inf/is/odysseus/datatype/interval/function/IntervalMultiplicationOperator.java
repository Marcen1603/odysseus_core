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

package de.uniol.inf.is.odysseus.datatype.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalMultiplicationOperator extends
		AbstractBinaryOperator<IntervalDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7746637195241980728L;

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public String getSymbol() {
		return "*";
	}

	@Override
	public IntervalDouble getValue() {
		IntervalDouble a = getInputValue(0);
		IntervalDouble b = getInputValue(1);
		return getValueInternal(a, b);
	}

	protected IntervalDouble getValueInternal(IntervalDouble a, IntervalDouble b) {
		final double inf = Math.min(
				Math.min(a.inf() * b.inf(), a.inf() * b.sup()),
				Math.min(a.sup() * b.inf(), a.sup() * b.sup()));
		final double sup = Math.max(
				Math.max(a.inf() * b.inf(), a.inf() * b.sup()),
				Math.max(a.sup() * b.inf(), a.sup() * b.sup()));
		return new IntervalDouble(inf, sup);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFIntervalDatatype.INTERVAL_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<IntervalDouble> operator) {
		return operator.getClass() == IntervalPlusOperator.class
				|| operator.getClass() == IntervalMinusOperator.class
				|| operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<IntervalDouble> operator) {
		return operator.getClass() == IntervalPlusOperator.class
				|| operator.getClass() == IntervalMinusOperator.class
				|| operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };

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
		return accTypes;
	}

}
