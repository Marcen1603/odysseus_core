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
public class IntervalIntersectionFunction extends
		AbstractFunction<IntervalDouble> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5436861306816764016L;
	private static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			accTypes1, accTypes1 };

	public IntervalIntersectionFunction() {
		super("intersection", 2, accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
	}

	@Override
	public IntervalDouble getValue() {
		IntervalDouble a = this.getInputValue(0);
		IntervalDouble b = this.getInputValue(1);
		double max = Math.max(a.inf(), b.inf());
		double min = Math.min(a.sup(), b.sup());
		if (max <= min) {
			return new IntervalDouble(max, min);
		} else {
			return new IntervalDouble(Double.NaN, Double.NaN);
		}
	}

}
