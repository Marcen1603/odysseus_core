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
public interface INonIncrementalAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends IAggregationFunction {

	/**
	 * Returns/calculates the values of the aggregation function.
	 * 
	 * <p>
	 * If this function does not have a result, this method should return an
	 * empty array (new Object[0]), not {@code null}!
	 * 
	 * @param elements
	 *            The elements that should be used for the calculation.
	 * @param trigger
	 *            The element that triggers the calculation.
	 * @param pointInTime
	 *            The point in time where this set of elements get valid.
	 * @return The result or an empty array.
	 */
	Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime);
}
