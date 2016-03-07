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
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * @author Cornelius Ludmann
 *
 */
public class OrderedIndexedByEndTsAggregationSweepArea<M extends ITimeInterval, T extends Tuple<M>> implements IAggregationSweepArea<M, T> {

	private static final long serialVersionUID = -7800228013013442982L;

	/**
	 * All elements indexed by end ts.
	 */
	private final TreeMap<PointInTime, List<T>> elements = new TreeMap<>();

	/**
	 * This field holds a view to the all elements until the element tree map is
	 * changed.
	 */
	private Collection<T> validTuplesView = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea#
	 * getOutdatedTuples(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Collection<T> getOutdatedTuples(final PointInTime pointInTime, final boolean remove) {
		if (remove) {
			validTuplesView = null;
		}
		return Collections.unmodifiableCollection(remove ? elements.remove(pointInTime) : elements.get(pointInTime));
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
		if (validTuplesView == null) {
			if (elements.size() == 0) {
				return Collections.emptyList();
			} else if (elements.size() == 1) {
				return elements.firstEntry().getValue();
			}
			validTuplesView = new Collection<T>() {

				private int size = -1;

				@Override
				public int size() {
					if (size == -1) {
						size = elements.values().stream().mapToInt(List::size).sum();
					}
					return size;
				}

				@Override
				public boolean isEmpty() {
					if (elements.isEmpty()) {
						return true;
					}
					for (final Iterator<List<T>> iter = elements.values().iterator(); iter.hasNext();) {
						if (!iter.next().isEmpty()) {
							return false;
						}
					}
					return true;
				}

				@Override
				public boolean contains(final Object o) {
					if (elements.isEmpty()) {
						return false;
					}
					for (final Iterator<List<T>> iter = elements.values().iterator(); iter.hasNext();) {
						if (iter.next().contains(o)) {
							return true;
						}
					}
					return false;
				}

				@Override
				public Iterator<T> iterator() {
					return new Iterator<T>() {

						private final Iterator<List<T>> iterOverValues = elements.values().iterator();
						private Iterator<T> iterOverElements = null;

						@Override
						public boolean hasNext() {
							if (iterOverElements == null && !iterOverElements.hasNext()) {
								return false;
							}
							if ((iterOverElements == null || !iterOverElements.hasNext()) && iterOverValues.hasNext()) {
								while (iterOverValues.hasNext() && !iterOverElements.hasNext()) {
									iterOverElements = iterOverValues.next().iterator();
								}
							}
							return iterOverElements.hasNext();
						}

						@Override
						public T next() {
							if (hasNext()) {
								iterOverElements.next();
							}
							return null;
						}

					};
				}

				@Override
				public Object[] toArray() {
					final List<Object> result = new ArrayList<>();
					for (final Iterator<T> iter = iterator(); iter.hasNext();) {
						result.add(iter.next());
					}
					return result.toArray();
				}

				@SuppressWarnings("unchecked")
				@Override
				public <E> E[] toArray(final E[] a) {
					return (E[]) toArray();
				}

				@Override
				public boolean add(final T e) {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean remove(final Object o) {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean containsAll(final Collection<?> c) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean addAll(final Collection<? extends T> c) {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean removeAll(final Collection<?> c) {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean retainAll(final Collection<?> c) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void clear() {
					throw new UnsupportedOperationException();
				}
			};
		}
		return validTuplesView;
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
				addElement(object);
				return true;
			}
		} else {
			addElement(object);
			return true;
		}
		return false;
	}

	/**
	 * @param object
	 */
	private void addElement(final T object) {
		List<T> list = elements.get(object.getMetadata().getEnd());
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(object);
	}

}
