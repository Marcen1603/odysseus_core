/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.TotallyAfterPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;

//should be K extends IPriority & ITimeInterval, but suns compiler (1.6) is buggy and doens't accept it:(
public class PriorityTISweepArea<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends DefaultTISweepArea<T> {
	public PriorityTISweepArea() {
		super();
		setRemovePredicate(TotallyAfterPredicate.getInstance());
	}

	@Override
	public void insert(T s) {
		synchronized (this.getElements()) {
			if (((IPriority) s.getMetadata()).getPriority() == 0) {
				this.getElements().addLast(s);
			} else {
				this.getElements().addFirst(s);
			}
		}
	}

	@Override
	public void purgeElements(T element, Order order) {
		if (((IPriority) element.getMetadata()).getPriority() > 0) {
			return;
		}
		Iterator<T> it = this.getElements().iterator();
		while (it.hasNext()) {
			T next = it.next();
			if (getRemovePredicate().evaluate(element, next)) {
				it.remove();
			} else {
				if (((IPriority) next.getMetadata()).getPriority() == 0) {
					return;
				}
			}
		}
	}

	@Override
	// TODO fehlerhaft im priortaetskontext wird aber atm nicht genutzt
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> it = this.getElements().iterator();
		switch (order) {
		case LeftRight:
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(element, next)) {
					it.remove();
					result.add(next);
					// } else {
					// return result.iterator();
				}
			}
			break;
		case RightLeft:
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(next, element)) {
					it.remove();
					result.add(next);
					// } else {
					// return result.iterator();
				}
			}
			break;
		}
		return result.iterator();
	}

	/**
	 * This method returns and removes all elements from the sweep area that
	 * have a time interval that lies totally before the passed interval.
	 * 
	 * @param validity
	 *            All elements with a time interval that lies totally before
	 *            this interval will be returned and removed from this
	 *            sweeparea.
	 * 
	 * @return
	 */
	@Override
	public Iterator<T> extractElementsBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		Iterator<T> li = getElements().iterator();
		while (li.hasNext()) {
			T s_hat = li.next();
			// Alle Elemente entfernen, die nicht mehr verschnitten werden
			// k�nnen (also davor liegen)
			if (TimeInterval.totallyBefore(s_hat.getMetadata(), validity)) {
				retval.add(s_hat);
				li.remove();
			} else {
				if (((IPriority) s_hat.getMetadata()).getPriority() == 0) {
					break;
				}
			}
		}
		return retval.iterator();
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		switch (order) {
		case LeftRight:
			for (T next : this.getElements()) {
				if (TimeInterval.totallyBefore(next.getMetadata(),
						element.getMetadata())) {
					continue;
				}
				if (TimeInterval.totallyAfter(next.getMetadata(),
						element.getMetadata())) {
					if (((IPriority) next.getMetadata()).getPriority() == 0) {
						break;
					} else {
						continue;
					}
				}
				if (getQueryPredicate().evaluate(element, next)) {
					result.add(next);
				}
			}
			break;
		case RightLeft:
			for (T next : this.getElements()) {
				if (TimeInterval.totallyBefore(next.getMetadata(),
						element.getMetadata())) {
					continue;
				}
				if (TimeInterval.totallyAfter(next.getMetadata(),
						element.getMetadata())) {
					if (((IPriority) next.getMetadata()).getPriority() == 0) {
						break;
					} else {
						continue;
					}
				}
				if (getQueryPredicate().evaluate(next, element)) {
					result.add(next);
				}
			}
			break;
		}
		return result.iterator();
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		Iterator<T> li = getElements().iterator();
		while (li.hasNext()) {
			T s_hat = li.next();
			// Alle Elemente entfernen, die nicht mehr verschnitten werden
			// k�nnen (also davor liegen)
			if (TimeInterval.totallyBefore(s_hat.getMetadata(), time)) {
				li.remove();
			} else {
				if (((IPriority) s_hat.getMetadata()).getPriority() == 0) {
					break;
				}
			}
		}
	}
}
