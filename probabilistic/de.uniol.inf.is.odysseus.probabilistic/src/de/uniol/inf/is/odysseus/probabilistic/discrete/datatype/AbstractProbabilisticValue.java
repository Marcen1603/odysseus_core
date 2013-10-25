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

package de.uniol.inf.is.odysseus.probabilistic.discrete.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <V>
 */
public abstract class AbstractProbabilisticValue<V> implements Serializable, IClone {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8024287769210294227L;
	/** The values. */
	private final Map<V, Double> values = new HashMap<V, Double>();

	/**
	 * Default constructor.
	 */
	public AbstractProbabilisticValue() {
	}

	/**
	 * Creates a new probabilistic value with the given probability.
	 * 
	 * @param value
	 *            The value
	 * @param probability
	 *            The probability
	 */
	public AbstractProbabilisticValue(final V value, final Double probability) {
		this.values.put(value, probability);
	}

	/**
	 * Creates a new probabilistic value with the given values.
	 * 
	 * @param values
	 *            The values
	 */
	public AbstractProbabilisticValue(final Map<V, Double> values) {
		this.values.putAll(values);
	}

	/**
	 * Creates a new probabilistic value from the given values and their probabilities.
	 * 
	 * @param values
	 *            The values
	 * @param probabilities
	 *            The associated probabilities
	 */
	public AbstractProbabilisticValue(final V[] values, final Double[] probabilities) {
		final int length = Math.min(values.length, probabilities.length);
		for (int i = 0; i < length; i++) {
			this.values.put(values[i], probabilities[i]);
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            The object to copy from
	 */
	public AbstractProbabilisticValue(final AbstractProbabilisticValue<V> other) {
		for (final Entry<V, Double> value : other.values.entrySet()) {
			this.values.put(value.getKey(), value.getValue());
		}
	}

	/**
	 * Gets the value of the values property.
	 * 
	 * @return The values with their probabilities
	 */
	public final Map<V, Double> getValues() {
		return this.values;
	}

	/*
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractProbabilisticValue<V> clone();

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (final Entry<V, Double> value : this.values.entrySet()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			sb.append(value.getKey()).append(":").append(value.getValue());
		}
		sb.append(")");
		return sb.toString();
	}

	/*
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (this.values != null) {
			result += this.values.hashCode();
		}
		return result;
	}

	/*
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() == obj.getClass()) {
			final AbstractProbabilisticValue<?> other = (AbstractProbabilisticValue<?>) obj;

			if (this.getValues().size() != other.getValues().size()) {
				return false;
			}
			for (final Entry<?, Double> thisEntry : this.getValues().entrySet()) {
				if (!other.getValues().containsKey(thisEntry.getKey())) {
					return false;
				}

				if (Math.abs(thisEntry.getValue() - other.getValues().get(thisEntry.getKey())) > 10E-9) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
