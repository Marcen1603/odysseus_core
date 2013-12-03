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
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticInteger;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDoubleToIntegerFunction extends AbstractProbabilisticFunction<ProbabilisticInteger> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1987170812000344574L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 1;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "doubleToInteger";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public final ProbabilisticInteger getValue() {
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (final Entry<?, Double> value : ((AbstractProbabilisticValue<?>) this.getInputValue(0)).getValues().entrySet()) {
			values.put(((Number) value.getKey()).intValue(), value.getValue());
		}
		return new ProbabilisticInteger(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_INTEGER;
	}

	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException("doubleToInteger has only 1 argument.");
		}
		return ProbabilisticDoubleToIntegerFunction.ACC_TYPES;
	}

}
