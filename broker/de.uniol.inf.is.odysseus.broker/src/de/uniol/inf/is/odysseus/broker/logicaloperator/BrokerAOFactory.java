package de.uniol.inf.is.odysseus.broker.logicaloperator;

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
		if(this.lastValue==currentTime){		
			currentTime++;
		}
		BrokerAO brokerAO = new BrokerAO(identifier);
		brokerAO.setGeneratedTime(currentTime);
		this.lastValue=currentTime;
		return brokerAO;
		
	}
	
	
	
	
}
