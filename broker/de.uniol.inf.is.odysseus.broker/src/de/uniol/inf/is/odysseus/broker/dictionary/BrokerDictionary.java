package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.transaction.TransactionType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerDictionary {
	private static BrokerDictionary instance; 
	private Map<String, BrokerDictionaryEntry> brokerlist = new HashMap<String, BrokerDictionaryEntry>();	
	
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
		return brokerlist.keySet().contains(name);		
	}	
			
	public void addBroker(String name, SDFAttributeList outputSchema){
		if(!brokerExists(name)){			
			//TODO: outputschma ist momentan noch gleich mit den anderen beiden 
			BrokerDictionaryEntry entry = new BrokerDictionaryEntry(name, outputSchema,outputSchema,outputSchema);
			this.brokerlist.put(name, entry);
		}
	}
			
	public int addNewWriteTransaction(String brokername, TransactionType.Write type){		
		return this.brokerlist.get(brokername).addNewWriteTransaction(type);		
	}
	
	public TransactionType.Write getTypeForPort(String brokername, int port){
		return this.brokerlist.get(brokername).getWriteType(port);
	}
	
	public SDFAttributeList getOutputSchema(String brokername){
		return this.brokerlist.get(brokername).getOutputSchema();
	}
}
