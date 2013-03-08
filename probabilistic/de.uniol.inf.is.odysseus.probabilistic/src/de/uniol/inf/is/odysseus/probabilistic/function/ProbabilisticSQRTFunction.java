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
package de.uniol.inf.is.odysseus.probabilistic.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticSQRTFunction extends
		AbstractProbabilisticFunction<ProbabilisticDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1757085950523543990L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
			SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
			SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
			SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
			SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public int getArity() {
		return 1;
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
		return "sqrt";
	}

	@Override
	public ProbabilisticDouble getValue() {
		AbstractProbabilisticValue<?> a = getInputValue(0);
		return getValueInternal(a);
	}

	protected ProbabilisticDouble getValueInternal(
			AbstractProbabilisticValue<?> a) {
		Map<Double, Double> values = new HashMap<Double, Double>(a.getValues()
				.size());
		for (Entry<?, Double> aEntry : a.getValues().entrySet()) {
			double value = FastMath.sqrt(((Number) aEntry.getKey())
					.doubleValue());
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

}
