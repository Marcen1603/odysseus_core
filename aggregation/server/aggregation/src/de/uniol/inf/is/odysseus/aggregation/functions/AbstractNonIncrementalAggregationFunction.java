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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractNonIncrementalAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractAggregationFunction<M, T> implements INonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = 8131921817847786812L;

	public AbstractNonIncrementalAggregationFunction() {
		super();
	}

	public AbstractNonIncrementalAggregationFunction(final AbstractAggregationFunction<M, T> other) {
		super(other);
	}

	public AbstractNonIncrementalAggregationFunction(final int[] attributes, final boolean needsAllAttributes,
			final String[] outputAttributeNames) {
		super(attributes, needsAllAttributes, outputAttributeNames);
	}

	public AbstractNonIncrementalAggregationFunction(final int[] attributes, final String[] outputAttributeNames) {
		super(attributes, outputAttributeNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * isIncremental()
	 */
	@Override
	public boolean isIncremental() {
		return false;
	}
}
