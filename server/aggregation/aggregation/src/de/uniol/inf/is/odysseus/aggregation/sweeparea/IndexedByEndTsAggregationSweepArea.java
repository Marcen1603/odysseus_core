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
package de.uniol.inf.is.odysseus.aggregation.sweeparea;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * This implementation indexed the elements by end TS with an
 * {@linkplain ArrayListMultimap}. The returned elements are not necessarily in
 * time order (with regard to the start TS).
 * 
 * @author Cornelius Ludmann
 *
 */
public class IndexedByEndTsAggregationSweepArea<M extends ITimeInterval, T extends Tuple<M>>
		implements IAggregationSweepArea<M, T> {

	private static final long serialVersionUID = -1584822247541286843L;

	private final Multimap<PointInTime, T> elements = ArrayListMultimap.create();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea#
	 * getOutdatedTuples(de.uniol.inf.is.odysseus.core.metadata.PointInTime,
	 * boolean)
	 */
	@Override
	public Collection<T> getOutdatedTuples(final PointInTime pointInTime, final boolean remove) {
		return Collections.unmodifiableCollection(remove ? elements.removeAll(pointInTime) : elements.get(pointInTime));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea#
	 * getValidTuples()
	 */
	@Override
	public Collection<T> getValidTuples() {
		return Collections.unmodifiableCollection(elements.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea#
	 * hasValidTuples()
	 */
	@Override
	public boolean hasValidTuples() {
		return !elements.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea#
	 * addElement(java.lang.Object, boolean)
	 */
	@Override
	public boolean addElement(final T object, final boolean onlyIfOutdating) {
		if (onlyIfOutdating) {
			if (!object.getMetadata().getEnd().isInfinite()) {
				elements.put(object.getMetadata().getEnd(), object);
				return true;
			}
		} else {
			elements.put(object.getMetadata().getEnd(), object);
			return true;
		}
		return false;
	}

}
