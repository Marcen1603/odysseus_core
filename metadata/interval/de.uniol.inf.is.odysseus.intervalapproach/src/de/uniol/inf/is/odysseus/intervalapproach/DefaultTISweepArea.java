package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.TotallyBeforePredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;

/**
 * This sweeparea implementation provides some optimizations on extract and
 * purge. The elements are always sorted and extract and purge only remove
 * elements, until the remove predicate first returns false. The remove
 * predicate is fixed to the TotallyBeforePredicate
 * 
 * @author Jonas Jacobi
 */
public class DefaultTISweepArea<T extends IMetaAttribute<? extends ITimeInterval>>
		extends SweepArea<T> implements Comparable<DefaultTISweepArea<T>> {
	private static final Logger logger = LoggerFactory.getLogger(ISweepArea.class);

	public DefaultTISweepArea() {
		super(new MetadataComparator<ITimeInterval>());
		super.setRemovePredicate(TotallyBeforePredicate.getInstance());
	}

	public Iterator<T> queryOverlaps(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized(elements){
			for (T s : elements) {
				if (TimeInterval.overlaps(s.getMetadata(), t)) {
					retval.add(s);
				}
			}
		}
		return retval.iterator();
	}

	@Override
	public void purgeElements(T element, Order order) {
		synchronized(elements){
			Iterator<T> it = this.elements.iterator();
			int i = 0;
	
			while (it.hasNext()) {
				if (getRemovePredicate().evaluate(it.next(), element)) {
					++i;
					it.remove();
				} else {
					if (logger.isTraceEnabled() && i > 0) {
						logger.trace("Purged " + i + " elements");
					}
					return;
				}
			}
		}
		//TODO hier muesste auch eine log ausgabe kommen, evtl. log auch einfach loeschen
	}

	@Override
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized(elements){
			Iterator<T> it = this.elements.iterator();
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(next, element)) {
					it.remove();
					result.add(next);
				} else {
					return result.iterator();
				}
			}
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
	public Iterator<T> extractElements(ITimeInterval validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized(elements){
			Iterator<T> li = elements.iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
				if (TimeInterval.totallyBefore(s_hat.getMetadata(), validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	/**
	 * @return the min start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the first element in the
	 *         linked list.
	 */
	public PointInTime getMinTs() {
		synchronized(elements){
			if (!this.elements.isEmpty()) {
				return this.elements.peek().getMetadata().getStart();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return the max start timestamp of all elements currently in the sweep area.
	 * Should be the start timestamp of the last element in the linked list.
	 */
	public PointInTime getMaxTs(){
		if(! this.elements.isEmpty()){
			return this.elements.getLast().getMetadata().getStart();
		}
		return null;
	}

	public int compareTo(DefaultTISweepArea<T> other) {
		if (this.getMinTs().before(other.getMinTs())) {
			return -1;
		} else if (this.getMinTs().after(other.getMinTs())) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * For Debug-Purposes
	 * 
	 * @param baseTime
	 * @return the current Content of the SweepArea with baseTime as origin of all points of time 
	 */
	public String getSweepAreaAsString(PointInTime baseTime) {
		StringBuffer buf = new StringBuffer("SweepArea " + elements.size()
				+ " Elems \n");
		for (T element : elements) {
			buf.append(element).append(" ");
			buf.append("{META ").append(
					element.getMetadata().toString(baseTime)).append("}\n");
		}
		return buf.toString();
	}
	
	@Override
	public void setRemovePredicate(IPredicate<? super T> removePredicate) {
		UnsupportedOperationException exception = new UnsupportedOperationException("Das remove-Praedikat in der DefaultTISweepArea ist fest. Es wird ein TotallyBeforePredicate verwendet.");
		exception.fillInStackTrace();
		throw exception;
	}
}
