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
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalPowerOperator extends AbstractBinaryOperator<IntervalDouble> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8262009270818213996L;

	public IntervalPowerOperator() {
		super("^",accTypes,SDFIntervalDatatype.INTERVAL_DOUBLE);
	}
	
	@Override
	public int getPrecedence() {
		return 1;
	}



	@Override
	public IntervalDouble getValue() {
		IntervalDouble a = getInputValue(0);
		double b = getNumericalInputValue(1);
		return getValueInternal(a, b);
	}

	protected IntervalDouble getValueInternal(IntervalDouble a, double b) {
		final double inf = Math.min(Math.pow(a.inf(), b), Math.pow(a.sup(), b));
		final double sup = Math.max(Math.pow(a.inf(), b), Math.pow(a.sup(), b));
		return new IntervalDouble(inf, sup);
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
	public boolean isLeftDistributiveWith(IOperator<IntervalDouble> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<IntervalDouble> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFIntervalDatatype.INTERVAL_BYTE,
					SDFIntervalDatatype.INTERVAL_SHORT,
					SDFIntervalDatatype.INTERVAL_INTEGER,
					SDFIntervalDatatype.INTERVAL_FLOAT,
					SDFIntervalDatatype.INTERVAL_DOUBLE,
					SDFIntervalDatatype.INTERVAL_LONG },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

}
