package de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator;

import de.uniol.inf.is.odysseus.broker.evaluation.dictionary.BrokerDictionary;

/**
 * A factory for creating BrokerAO objects.
 * 
 * @author Dennis Geesen
 */
public class BrokerAOFactory {

	/** The factory. */
	private static BrokerAOFactory factory;
		
	/** The last value which has been given to a broker. */
	private long lastValue;
	
	
	/**
	 * Instantiates a new factory.
	 */
	private BrokerAOFactory() {
		this.lastValue = System.currentTimeMillis();
	}
	
	/**
	 * Gets the factory.
	 *
	 * @return the factory
	 */
	public static synchronized BrokerAOFactory getFactory(){
		if(factory==null){
			factory = new BrokerAOFactory();			
		}
		return factory;
	}
	
	/**
	 * Creates a new {@link BrokerAO}.
	 * Assures that each created {@link BrokerAO} (including with same identifier) has a unique value.
	 *
	 *
	 * @param identifier the identifier
	 * @return the created broker
	 */
	public BrokerAO createBrokerAO(String identifier){
		// create a really unique identifier
		long currentTime = System.currentTimeMillis();
		while(this.lastValue==currentTime){		
			currentTime++;
		}
		this.lastValue=currentTime;
		BrokerAO brokerAO = new BrokerAO(identifier);
		brokerAO.setGeneratedTime(currentTime);
		if(BrokerDictionary.getInstance().brokerExists(identifier)){
			brokerAO.setSchema(BrokerDictionary.getInstance().getSchema(identifier));
			brokerAO.setQueueSchema(BrokerDictionary.getInstance().getQueueSchema(identifier));
		}
		return brokerAO;		
	}
	
	
	
	
}
