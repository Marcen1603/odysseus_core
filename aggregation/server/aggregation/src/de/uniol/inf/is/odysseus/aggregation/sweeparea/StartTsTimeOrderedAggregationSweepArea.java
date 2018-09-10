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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * @author Cornelius Ludmann
 *
 */
public class StartTsTimeOrderedAggregationSweepArea<M extends ITimeInterval, T extends Tuple<M>>
		implements IAggregationSweepArea<M, T> {

	private static final long serialVersionUID = -6642572267921244531L;

	private final ArrayList<T> elements = new ArrayList<>();
	private final Multimap<PointInTime, T> outdatingElements = ArrayListMultimap.create();

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
		if (remove) {
			elements.removeIf(e -> e.getMetadata().getEnd().equals(pointInTime));
		}
		return Collections.unmodifiableCollection(
				remove ? outdatingElements.removeAll(pointInTime) : outdatingElements.get(pointInTime));
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
		return Collections.unmodifiableCollection(elements);
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
		if(onlyIfOutdating) {
			if(!object.getMetadata().getEnd().isInfinite()) {
				elements.add(object);
				outdatingElements.put(object.getMetadata().getEnd(), object);
				return true;
			}
		} else {
			elements.add(object);
			if(!object.getMetadata().getEnd().isInfinite()) {
				outdatingElements.put(object.getMetadata().getEnd(), object);
			}
			return true;
		}
		return false;
	}

}
