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
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleStatefulAggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleStatelessAggregateFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public class Count<M extends ITimeInterval, T extends Tuple<M>> extends AbstractSimpleStatefullAggregateFunction<M, T>
		implements Cloneable, SimpleStatelessAggregateFunction<M, T> {

	private long count = 0;

	public Count() {
		super();
	}
	
	protected Count(Count<M,T> copy) {
		super();
		outputAttribute = copy.outputAttribute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.
	 * functions.SimpleStatefulAggregateFunction#evaluate(java.util.List,
	 * java.util.List)
	 */
	@Override
	public synchronized Long evaluate(T newElement, List<T> outdatedElements) {
		if (newElement != null) {
			++count;
		}
		if (outdatedElements != null) {
			count -= outdatedElements.size();
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleStatelessAggregateFunction#evaluate(java.util.List)
	 */
	@Override
	public Object evaluate(List<T> elements) {
		return elements.size();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.AbstractSimpleStatefullAggregateFunction#clone()
	 */
	@Override
	public SimpleStatefulAggregateFunction<M, T> clone() {
		return new Count<>(this);
	}
}
