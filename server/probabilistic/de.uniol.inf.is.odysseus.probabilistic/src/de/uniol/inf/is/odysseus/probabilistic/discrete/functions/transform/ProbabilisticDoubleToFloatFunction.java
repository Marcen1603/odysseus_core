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
package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticFloat;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDoubleToFloatFunction extends
		AbstractProbabilisticFunction<ProbabilisticFloat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5545298100153510227L;

	public ProbabilisticDoubleToFloatFunction() {
		super("doubleToFloat", 1, ACC_TYPES,
				SDFProbabilisticDatatype.PROBABILISTIC_FLOAT);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final ProbabilisticFloat getValue() {
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (final Entry<?, Double> value : ((AbstractProbabilisticValue<?>) this
				.getInputValue(0)).getValues().entrySet()) {
			values.put(((Number) value.getKey()).floatValue(), value.getValue());
		}
		return new ProbabilisticFloat(values);
	}

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE };

}
