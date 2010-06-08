package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.broker.physicaloperator.predicate.AttributeTimeIntervalComparator;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;

/**
 * The BrokerSweepArea allows to save the current valid content for the broker.
 * 
 * @author Dennis Geesen
 *
 * @param <T> the type of a tuple
 */
public class BrokerSweepArea <T extends IMetaAttributeContainer<? extends ITimeInterval>> extends SweepArea<T>{
	
	/**
	 * Instantiates a new SweepArea.
	 */
	@SuppressWarnings("unchecked")
	public BrokerSweepArea() {
		super(new AttributeTimeIntervalComparator());
	}
	
	/**
	 * Instantiates a new BrokerSweepArea from an old one.
	 *
	 * @param brokerTISweepArea the old one
	 * @ 
	 */
	public BrokerSweepArea(BrokerSweepArea<T> brokerTISweepArea) {
		super(brokerTISweepArea);
	}

	

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea#purgeElements(de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer, de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order)
	 */
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
					//return;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea#extractElements(de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer, de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order)
	 */
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
					//return result.iterator();
				}
			}
		}
		return result.iterator();
	}

	
	/**
	 * Gets the SweepArea as string.
	 *
	 * @param baseTime the base time
	 * @return the SweepArea as string
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
	
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea#clone()
	 */
	@Override
	public BrokerSweepArea<T> clone()  {
		return new BrokerSweepArea<T>(this);
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized(elements){
			Iterator<T> it = this.elements.iterator();
			while (it.hasNext()) {
				T next = it.next();
				if (next.getMetadata().getStart().before(time)) {
					it.remove();
					result.add(next);
				} else {
					//return result.iterator();
				}
			}
		}
		return result.iterator();
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		synchronized(elements){
			Iterator<T> it = this.elements.iterator();
			while (it.hasNext()) {
				T next = it.next();
				if (next.getMetadata().getStart().before(time)) {
					it.remove();
				}
		
			}
		}
	}

}
