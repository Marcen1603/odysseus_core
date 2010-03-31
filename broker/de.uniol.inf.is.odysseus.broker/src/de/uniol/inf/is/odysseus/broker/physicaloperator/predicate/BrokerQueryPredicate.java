package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class BrokerQueryPredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {
	
	private static final long serialVersionUID = 759591029481942568L;
	private BrokerEqualPredicate<T> brokerEqualPredicate;
	
	public BrokerQueryPredicate(){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>();
	}
	
	
	public BrokerQueryPredicate(int position){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>(position);
	}
	
	@Override
	public boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(T left, T right) {		 
		return brokerEqualPredicate.evaluate(left, right);		
	}

}
