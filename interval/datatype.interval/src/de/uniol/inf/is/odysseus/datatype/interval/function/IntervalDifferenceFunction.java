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
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;

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
	private static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "difference";
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

	@Override
	public SDFDatatype getReturnType() {
		return SDFIntervalDatatype.INTERVAL_DOUBLE;
	}

	private boolean intersects(final IntervalDouble a, final IntervalDouble b) {
		return ((!a.isEmpty()) && (!b.isEmpty()) && (b.inf() <= a.sup()) && (a
				.inf() <= b.sup()));
	}
}
