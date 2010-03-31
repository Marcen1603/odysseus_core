package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;

public class BrokerSweepArea <T extends IMetaAttributeContainer<? extends ITimeInterval>> extends SweepArea<T>{
	public BrokerSweepArea() {
		super(new MetadataComparator<ITimeInterval>());		
	}
	
	public BrokerSweepArea(BrokerSweepArea<T> brokerTISweepArea){
		super(brokerTISweepArea);
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
					//return;
				}
			}
		}
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
					//return result.iterator();
				}
			}
		}
		return result.iterator();
	}

	
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
	public BrokerSweepArea<T> clone() throws CloneNotSupportedException {
		return new BrokerSweepArea<T>(this);
	}

}
