package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

//should be K extends IPriority & ITimeInterval, but suns compiler (1.6) is buggy and doens't accept it:(
public class PriorityTISweepArea<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends DefaultTISweepArea<T> {
	public PriorityTISweepArea() {
		super();
	}

	@Override
	public void insert(T s) {
		synchronized (this.elements) {
			if (((IPriority)s.getMetadata()).getPriority() == 0) {
				this.elements.addLast(s);
			} else {
				this.elements.addFirst(s);
			}
		}
	};

	@Override
	public void purgeElements(T element, Order order) {
		if (((IPriority)element.getMetadata()).getPriority() > 0) {
			return;
		}
		Iterator<T> it = this.elements.iterator();
		while (it.hasNext()) {
			T next = it.next();
			if (getRemovePredicate().evaluate(next, element)) {
				it.remove();
			} else {
				if (((IPriority)next.getMetadata()).getPriority() == 0) {
					return;
				}
			}
		}
	}

	@Override
	// TODO fehlerhaft im priortaetskontext wird aber atm nicht genutzt
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> it = this.elements.iterator();
		switch (order) {
		case LeftRight:
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(element, next)) {
					it.remove();
					result.add(next);
				} else {
					return result.iterator();
				}
			}
			break;
		case RightLeft:
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(next, element)) {
					it.remove();
					result.add(next);
				} else {
					return result.iterator();
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
	public Iterator<T> extractElements(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		Iterator<T> li = elements.iterator();
		while (li.hasNext()) {
			T s_hat = li.next();
			// Alle Elemente entfernen, die nicht mehr verschnitten werden
			// k�nnen (also davor liegen)
			if (TimeInterval.totallyBefore(s_hat.getMetadata(), validity)) {
				retval.add(s_hat);
				li.remove();
			} else {
				if (((IPriority)s_hat.getMetadata()).getPriority() == 0) {
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
			for (T next : this.elements) {
				if (TimeInterval.totallyBefore(next.getMetadata(), element
						.getMetadata())) {
					continue;
				}
				if (TimeInterval.totallyAfter(next.getMetadata(), element
						.getMetadata())) {
					if (((IPriority)next.getMetadata()).getPriority() == 0) {
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
			for (T next : this.elements) {
				if (TimeInterval.totallyBefore(next.getMetadata(), element
						.getMetadata())) {
					continue;
				}
				if (TimeInterval.totallyAfter(next.getMetadata(), element
						.getMetadata())) {
					if (((IPriority)next.getMetadata()).getPriority() == 0) {
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
}
