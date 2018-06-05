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
package de.uniol.inf.is.odysseus.aggregation.functions;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractIncrementalAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractAggregationFunction<M, T> implements IIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = 5543450230865786656L;

	public AbstractIncrementalAggregationFunction() {
		super();
	}

	public AbstractIncrementalAggregationFunction(final AbstractAggregationFunction<M, T> other) {
		super(other);
	}

	public AbstractIncrementalAggregationFunction(final int[] attributes, final boolean needsAllAttributes,
			final String[] outputAttributeNames) {
		super(attributes, needsAllAttributes, outputAttributeNames);
	}

	public AbstractIncrementalAggregationFunction(final int[] attributes, final String[] outputAttributeNames) {
		super(attributes, outputAttributeNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#addNewAndEvaluate(de.uniol.inf.is.
	 * odysseus.core.collection.Tuple)
	 */
	@Override
	public Object[] addNewAndEvaluate(final T newElement) {
		addNew(newElement);
		return evalute(newElement, newElement.getMetadata().getStart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#removeOutdatedAndEvaluate(java.util.
	 * Collection, de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Object[] removeOutdatedAndEvaluate(final Collection<T> outdatedElements, final T trigger,
			final PointInTime pointInTime) {
		removeOutdated(outdatedElements, trigger, pointInTime);
		return evalute(trigger, pointInTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * isIncremental()
	 */
	@Override
	public boolean isIncremental() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractIncrementalAggregationFunction<M, T> clone();
}
