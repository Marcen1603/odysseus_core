package de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.association;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;


/**
 * BrokerJoinTIPO is a special case of JoinTIPO for the broker. 
 * It is important on which port the broker is connected. Therefore the broker has to be on the left input (port 0). 
 * The difference is the merge function for the metadata {@link BrokerMetadataMergeFunction} 
 * and the behavior of the SweepArea.
 * The merge function will not set the new timestamp of a merged object to the intersection, 
 * but set it to the timestamp of the object coming from the right input.
 * The SweepArea of the left input will be cleared after two objects have been successfully merged 
 * and the merged object will be removed from the right SweepArea as well. Thus this Join estimates 
 * the whole broker content for each new object coming from the right input.
 *
 * @author Dennis Geesen
 *
 * @param <K> the type of the metadata (based on a time interval)
 * @param <T> the type of the tuple
 */
public class BrokerJoinTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends JoinTIPO<K,T> {

	/** The otherport. */
	private int otherport = 0;
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO#process_next(de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer, int)
	 */
	@Override
	protected void process_next(T object, int port) {	
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

			while (qualifies.hasNext()) {
				T next = qualifies.next();			
				getAreas()[port].remove(object);
				getAreas()[otherport].remove(next);			
				T newElement = merge(object, next, order);
				transfer(newElement);
				//transferFunction.transfer(newElement);			
				// i don't need elements from broker anymore
				getAreas()[0].clear();
				
			}		
		}
	}
}
