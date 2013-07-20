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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class SumPartialAggregate<T> implements IPartialAggregate<T> {
	/** The value of the aggregate. */
	private double sum = 0;
	/** The result data type. */
	private final String datatype;

	/**
	 * Default constructor.
	 * 
	 * @param datatype
	 *            The result datatype
	 */
	public SumPartialAggregate(final String datatype) {
		this.sum = 0.0;
		this.datatype = datatype;
	}

	/**
	 * Creates a new partial aggregate with the given value.
	 * 
	 * @param value
	 *            The value
	 * @param probability
	 *            The probability
	 * @param datatype
	 *            The result datatype
	 */
	public SumPartialAggregate(final double value, final double probability, final String datatype) {
		this.sum = value * probability;
		this.datatype = datatype;
	}

	/**
	 * Creates a new partial aggregate with the given value.
	 * 
	 * @param sum
	 *            The value of the sum
	 * @param datatype
	 *            The result datatype
	 */
	public SumPartialAggregate(final double sum, final String datatype) {
		this.sum = sum;
		this.datatype = datatype;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param sumPartialAggregate
	 *            The object to copy from
	 */
	public SumPartialAggregate(final SumPartialAggregate<T> sumPartialAggregate) {
		this.sum = sumPartialAggregate.sum;
		this.datatype = sumPartialAggregate.datatype;
	}

	/**
	 * Gets the value of the sum property.
	 * 
	 * @return the sum
	 */
	public final double getSum() {
		return this.sum;
	}

	/**
	 * Add the given value to the aggregate.
	 * 
	 * @param value
	 *            The value
	 * @param probability
	 *            The value to add
	 */
	public final void add(final double value, final double probability) {
		this.sum += value * probability;
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuffer ret = new StringBuffer("SumPartialAggregate (").append(this.hashCode()).append(")").append(this.sum);
		return ret.toString();
	}

	/*
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final SumPartialAggregate<T> clone() {
		return new SumPartialAggregate<T>(this);
	}
}
