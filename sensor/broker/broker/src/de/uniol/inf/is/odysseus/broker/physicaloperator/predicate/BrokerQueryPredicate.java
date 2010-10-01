package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

/**
 * The BrokerQueryPredicate represents the query predicate for a SweepArea.
 * In this case it uses a {@link BrokerEqualPredicate} to evaluate to elements.
 *
 * @author Dennis Geesen
 *
 * @param <T> the generic type
 */
public class BrokerQueryPredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 759591029481942568L;
	
	/** The broker equal predicate. */
	private BrokerEqualPredicate<T> brokerEqualPredicate;
	
	/**
	 * Instantiates a new broker query predicate which will evaluate the whole element.
	 */
	public BrokerQueryPredicate(){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>();
	}
	
	
	/**
	 * Instantiates a new broker query predicate which will only evaluate the attribute of a given position.
	 *
	 * @param position the position
	 */
	public BrokerQueryPredicate(int position){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>(position);
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.predicate.IPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.predicate.IPredicate#evaluate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(T left, T right) {		 
		return brokerEqualPredicate.evaluate(left, right);		
	}


	@Override
	public BrokerQueryPredicate<T> clone() {
		BrokerQueryPredicate<T> clone = new  BrokerQueryPredicate<T>();
		clone.brokerEqualPredicate = this.brokerEqualPredicate.clone();
		return clone;
	}

}
