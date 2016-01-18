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
package de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.AbstractSimpleAggregateFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractSimpleStatefullAggregateFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractSimpleAggregateFunction<M, T> implements SimpleStatefulAggregateFunction<M, T> {

	/**
	 * 
	 */
	public AbstractSimpleStatefullAggregateFunction() {
		super();
	}

	public AbstractSimpleStatefullAggregateFunction(int[] attributes) {
		super(attributes);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational_interval.simpleaggregate.functions.
	 * SimpleAggregateFunction#evaluate(java.util.List, java.util.List,
	 * java.util.List)
	 */
	@Override
	public Object evaluate(List<T> elements, T newElement, List<T> outdatedElements) {
		return evaluate(newElement, outdatedElements);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SimpleStatefulAggregateFunction<M,T> clone() {
		return null;
	}
}
