package de.uniol.inf.is.odysseus.broker.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

/**
 * The BrokerMetadata implements a default {@link IBrokerInterval}.
 * 
 * @author Dennis Geesen
 */
public class BrokerMetadata extends TimeInterval implements IBrokerInterval {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9106037522259783069L;

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.intervalapproach.TimeInterval#clone()
	 */
	public BrokerMetadata clone(){
		BrokerMetadata clone = new BrokerMetadata();		
		return clone;
	}
	
}
