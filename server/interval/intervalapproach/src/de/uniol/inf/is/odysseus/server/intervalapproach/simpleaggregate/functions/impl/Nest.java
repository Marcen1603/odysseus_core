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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.AbstractSimpleStatelessAggregateFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public class Nest<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractSimpleStatelessAggregateFunction<M, T> {

	/**
	 * 
	 */
	public Nest() {
		super();
	}
	
	public Nest(int[] attributes) {
		super(attributes);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational_interval.simpleaggregate.functions.
	 * SimpleStatelessAggregateFunction#evaluate(java.util.List)
	 */
	@Override
	public Object evaluate(List<T> elements) {
		if (this.isAllAttributes()) {
			if(elements instanceof LinkedList) {
				return ((LinkedList<T>) elements).clone();
			} else if(elements instanceof ArrayList) {
				return ((ArrayList<T>) elements).clone();
			}
			return new ArrayList<>(elements);
		} else {
			// TODO: In case of not isAllAttributes: Use better a stateful implementation!
			List<T> results = new ArrayList<>(elements.size());
			for (T e : elements) {
				results.add(this.getAttributesAsTuple(e));
			}
			return results;
		}
	}

}
