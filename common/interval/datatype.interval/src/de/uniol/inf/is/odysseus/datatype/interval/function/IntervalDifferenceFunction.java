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
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalDifferenceFunction extends
		AbstractFunction<IntervalDouble> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4670597155345841395L;
	private static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1,accTypes1};

	public IntervalDifferenceFunction() {
		super("difference",2,accTypes,SDFIntervalDatatype.INTERVAL_DOUBLE);
	}
	

	@Override
	public IntervalDouble getValue() {
		IntervalDouble a = this.getInputValue(0);
		IntervalDouble b = this.getInputValue(1);
		if (!this.intersects(a, b)) {
			return new IntervalDouble(Double.MAX_VALUE, Double.MIN_VALUE);
		}
		if ((b.inf() >= a.inf()) && (b.sup() <= a.sup())) {
			return null;
		}
		if ((b.inf() <= a.inf()) && (b.sup() <= a.sup())) {
			return new IntervalDouble(b.sup(), a.sup());
		}
		if (b.inf() >= a.inf()) {
			return new IntervalDouble(a.inf(), b.inf());
		}
		return new IntervalDouble(Double.MAX_VALUE, Double.MIN_VALUE);
	}

	private boolean intersects(final IntervalDouble a, final IntervalDouble b) {
		return ((!a.isEmpty()) && (!b.isEmpty()) && (b.inf() <= a.sup()) && (a
				.inf() <= b.sup()));
	}
}
