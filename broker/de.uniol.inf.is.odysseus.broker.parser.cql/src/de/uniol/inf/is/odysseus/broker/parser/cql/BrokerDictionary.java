package de.uniol.inf.is.odysseus.broker.parser.cql;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;

public class BrokerDictionary {
	private static BrokerDictionary instance; 
	private Map<String, BrokerAO> brokerlist = new HashMap<String, BrokerAO>();
	
	
	private BrokerDictionary(){
		
	}
	
	public static synchronized BrokerDictionary getInstance(){
		if(instance == null)
		{
			instance = new BrokerDictionary();
		}
		return instance;
	}
	
	public boolean brokerExists(String name){
		return brokerlist.containsKey(name);
	}
	
	public BrokerAO getBroker(String name){
		return this.brokerlist.get(name);
	}
	
	public void addBroker(String name, BrokerAO broker){
		if(!brokerExists(name)){
			this.brokerlist.put(name, broker);
		}
	}
}
