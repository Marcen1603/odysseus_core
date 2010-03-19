package de.uniol.inf.is.odysseus.broker.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class BrokerMetadata extends TimeInterval implements IBrokerInterval {

	
	private static final long serialVersionUID = 9106037522259783069L;

	public BrokerMetadata clone(){
		BrokerMetadata clone = new BrokerMetadata();		
		return clone;
	}
	
}
