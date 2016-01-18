/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.impl;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.AbstractSimpleStatefullAggregateFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public class Sum<M extends ITimeInterval, T extends Tuple<M>> extends AbstractSimpleStatefullAggregateFunction<M, T>
		implements Cloneable {

	private double sum = 0;

	/**
	 * 
	 */
	public Sum(int[] attributes) {
		super(attributes);
		if (attributes.length != 1) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param sum2
	 */
	public Sum(Sum<M, T> copy) {
		super(copy.attributes);
		outputAttribute = copy.outputAttribute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational_interval.simpleaggregate.functions.
	 * SimpleStatefulAggregateFunction#evaluate(java.util.List, java.util.List)
	 */
	@Override
	public synchronized Double evaluate(T newElement, List<T> outdatedElements) {
		if (newElement != null) {
			Object[] e2 = getAttributes(newElement);
			sum += (Double) e2[0];
		}

		if (outdatedElements != null) {
			for (T e : outdatedElements) {
				Object[] e2 = getAttributes(e);
				sum -= (Double) e2[0];
			}
		}

		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Sum<M, T> clone() {
		return new Sum<M, T>(this);
	}
}
