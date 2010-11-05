package de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

/**
 * The BrokerRemovePredicate provides the remove predicate for a SweepArea.
 * Elements qualify if they are equal and the left element starts before the right element.
 * 
 * @author Dennis Geesen
 *
 * @param <T> the generic type
 */
public class BrokerRemovePredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9062468057316937953L;
	
	/** The equal predicate. */
	private BrokerEqualPredicate<T> brokerEqualPredicate;
	
	/**
	 * Instantiates a new remove predicate which will evaluate the whole element.
	 */
	public BrokerRemovePredicate(){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>();
	}
	
	/**
	 * Instantiates a new remove predicate which will only evaluate the attribute of a given position.
	 *
	 * @param position the position
	 */
	public BrokerRemovePredicate(int position){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>(position);
	}
	
	public BrokerRemovePredicate(BrokerRemovePredicate<T> brokerRemovePredicate) {
		this.brokerEqualPredicate = brokerRemovePredicate.brokerEqualPredicate.clone();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.predicate.IPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(T input) {		
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.predicate.IPredicate#evaluate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(T left, T right) {		
		
			// wirklich t1_s >= t2_e?!
			//if(right.getMetadata().getEnd().beforeOrEquals(left.getMetadata().getStart())){
			if(left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart())){
				return true;
			}
			return false;
	}
	
	@Override
	public BrokerRemovePredicate<T> clone(){
		return new BrokerRemovePredicate<T>(this);
	}

//	@Override
//	public BrokerRemovePredicate<T> clone() {
//		BrokerRemovePredicate<T> clone = new BrokerRemovePredicate<T>();
//		clone.brokerEqualPredicate = this.brokerEqualPredicate.clone();
//		return clone;
//	}

	
}
