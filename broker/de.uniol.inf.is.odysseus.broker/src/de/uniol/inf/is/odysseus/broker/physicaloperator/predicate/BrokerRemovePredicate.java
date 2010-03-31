package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class BrokerRemovePredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {
	
	private static final long serialVersionUID = 9062468057316937953L;
	private BrokerEqualPredicate<T> brokerEqualPredicate;
	
	public BrokerRemovePredicate(){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>();
	}
	
	public BrokerRemovePredicate(int position){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>(position);
	}
	
	@Override
	public boolean evaluate(T input) {		
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(T left, T right) {		
		if(this.brokerEqualPredicate.evaluate(left, right)){
			// wirklich t1_s >= t2_e?!
			//if(right.getMetadata().getEnd().beforeOrEquals(left.getMetadata().getStart())){
			if(left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart())){
				return true;
			}
		}
		return false;
	}

	
}
