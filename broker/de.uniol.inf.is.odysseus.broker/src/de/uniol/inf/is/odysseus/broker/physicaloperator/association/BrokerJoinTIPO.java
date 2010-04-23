package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;

public class BrokerJoinTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends JoinTIPO<K,T> {

	private int otherport = 0;
	
	@Override
	protected void process_next(T object, int port) {	
		storage.setCurrentPort(port);
		if (isDone()) { 
			return;
		}
		otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		synchronized (this.getAreas()[otherport]) {
			getAreas()[otherport].purgeElements(object, order);
		}

		synchronized (this) {		
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		Iterator<T> qualifies;
		synchronized (this.getAreas()) {
			synchronized (this.getAreas()[otherport]) {
				qualifies = getAreas()[otherport].queryCopy(object, order);
			}
			transferFunction.newElement(object, port);
			synchronized (getAreas()[port]) {
				getAreas()[port].insert(object);				
			}
		}

		while (qualifies.hasNext()) {
			T next = qualifies.next();			
			getAreas()[port].remove(object);
			getAreas()[otherport].remove(next);			
			T newElement = merge(object, next, order);
			transferFunction.transfer(newElement);			
			// i don't need elements from broker anymore
			getAreas()[0].clear();
			
		}		
		
		synchronized (this.getAreas()) {
			storage.updatePunctuationData(object);
		}
		
	}
}
