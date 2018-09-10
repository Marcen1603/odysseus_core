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
public class IntervalMinusOperator extends AbstractBinaryOperator<IntervalDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7665159518653289431L;

	public IntervalMinusOperator() {
		super("-",accTypes,SDFIntervalDatatype.INTERVAL_DOUBLE);
	}
	
	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public IntervalDouble getValue() {
		IntervalDouble a = getInputValue(0);
		IntervalDouble b = getInputValue(1);
		return getValueInternal(a, b);
	}

	protected IntervalDouble getValueInternal(IntervalDouble a, IntervalDouble b) {
		return new IntervalDouble(a.inf() - b.sup(), a.sup() - b.inf());
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
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
	public boolean isLeftDistributiveWith(IOperator<IntervalDouble> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<IntervalDouble> operator) {
		return false;
	}

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1, accTypes1}; 

}
