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
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticSQRTFunction extends AbstractProbabilisticFunction<ProbabilisticDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1757085950523543990L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticSQRTFunction.accTypes;
	}

	@Override
	public String getSymbol() {
		return "sqrt";
	}

	@Override
	public ProbabilisticDouble getValue() {
		final AbstractProbabilisticValue<?> a = this.getInputValue(0);
		return this.getValueInternal(a);
	}

	protected ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a) {
		final Map<Double, Double> values = new HashMap<Double, Double>(a.getValues().size());
		for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
			final double value = FastMath.sqrt(((Number) aEntry.getKey()).doubleValue());
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
