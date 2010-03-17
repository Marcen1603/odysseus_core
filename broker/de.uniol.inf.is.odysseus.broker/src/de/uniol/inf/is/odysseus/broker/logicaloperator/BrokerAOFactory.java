package de.uniol.inf.is.odysseus.broker.logicaloperator;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;

public class BrokerAOFactory {

	private static BrokerAOFactory factory;
		
	private long lastValue;
	
	
	private BrokerAOFactory() {
		this.lastValue = System.currentTimeMillis();
	}
	
	public static synchronized BrokerAOFactory getFactory(){
		if(factory==null){
			factory = new BrokerAOFactory();			
		}
		return factory;
	}
	
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
