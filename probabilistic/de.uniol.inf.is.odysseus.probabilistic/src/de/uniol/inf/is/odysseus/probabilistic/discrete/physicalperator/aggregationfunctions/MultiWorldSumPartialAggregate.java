/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class MultiWorldSumPartialAggregate<T> implements IPartialAggregate<T> {
	/** The value of the aggregate. */
	private AbstractProbabilisticValue<?> aggregate;
	/** The result data type. */
	private final String datatype;

	/**
	 * Default constructor.
	 * 
	 * @param datatype
	 *            The result datatype
	 */
	public MultiWorldSumPartialAggregate(final String datatype) {
		this.aggregate = new ProbabilisticDouble(0.0, 1.0);
		this.datatype = datatype;
	}

	/**
	 * Creates a new partial aggregate with the given value.
	 * 
	 * @param aggregate
	 *            The aggregate
	 * @param datatype
	 *            The result datatype
	 */
	public MultiWorldSumPartialAggregate(final AbstractProbabilisticValue<?> aggregate, final String datatype) {
		this.datatype = datatype;
		this.add(aggregate);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param sumPartialAggregate
	 *            The object to copy from
	 */
	public MultiWorldSumPartialAggregate(final MultiWorldSumPartialAggregate<T> sumPartialAggregate) {
		this.aggregate = sumPartialAggregate.aggregate;
		this.datatype = sumPartialAggregate.datatype;
	}

	/**
	 * Gets the value of the aggregate.
	 * 
	 * @return the aggregate
	 */
	public final AbstractProbabilisticValue<?> getAggregate() {
		return this.aggregate;
	}

	/**
	 * Add the given value to the aggregate.
	 * 
	 * @param value
	 *            The value
	 */
	public final void add(final AbstractProbabilisticValue<?> value) {
		final Map<Double, Double> newValues = new HashMap<Double, Double>(this.aggregate.getValues().size() * value.getValues().size());
		for (final Entry<?, Double> sumEntry : this.aggregate.getValues().entrySet()) {
			for (final Entry<?, Double> valueEntry : value.getValues().entrySet()) {
				final double newValue = ((Number) sumEntry.getKey()).doubleValue() + ((Number) valueEntry.getKey()).doubleValue();
				if (newValues.containsKey(newValue)) {
					newValues.put(newValue, newValues.get(newValue) + (sumEntry.getValue() * valueEntry.getValue()));
				} else {
					newValues.put(newValue, sumEntry.getValue() * valueEntry.getValue());
				}
			}
		}
		this.aggregate = new ProbabilisticDouble(newValues);
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuffer ret = new StringBuffer("MultiWorldSumPartialAggregate (").append(this.hashCode()).append(")").append(this.aggregate);
		return ret.toString();
	}

	/*
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final MultiWorldSumPartialAggregate<T> clone() {
		return new MultiWorldSumPartialAggregate<T>(this);
	}
}
