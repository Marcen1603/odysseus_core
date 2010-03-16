package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
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
		
	public int getNextWritePort(String brokername){
		//normal transactions are continuous
		return this.brokerlist.get(brokername).addNewWriteTransaction(WriteTransaction.Continuous);
	}
	
	public WriteTransaction getWriteTypeForPort(String brokername, int port){
		return this.brokerlist.get(brokername).getWriteType(port);
	}
	
	public void setWriteTypeForPort(String brokername, int port, WriteTransaction type){
		this.brokerlist.get(brokername).setWriteType(port, type);
	}
	
	public int getNextReadPort(String brokername){		
		return this.brokerlist.get(brokername).addNewReadTransaction(ReadTransaction.Continuous);
	}
	
	public void resetAllWritingPorts(String brokername, WriteTransaction type){
		this.brokerlist.get(brokername).resetAllWritingPorts(type);
	}
	
	public ReadTransaction getReadTypeForPort(String brokername, int port){
		return this.brokerlist.get(brokername).getReadType(port);
	}
	
	public SDFAttributeList getOutputSchema(String brokername){
		return this.brokerlist.get(brokername).getOutputSchema();
	}

	public void setReadTypeForPort(String brokername, int port, ReadTransaction type) {
		this.brokerlist.get(brokername).setReadType(port, type);		
	}
	
	public void resetAllReadingPorts(String brokername, ReadTransaction type){
		this.brokerlist.get(brokername).resetAllReadingPorts(type);
	}
	
	public ReadTransaction[] getAllReadingPorts(String brokername){
		return this.brokerlist.get(brokername).getReadingPorts();
	}
	
	public void removeAllReadingPorts(String brokername){
		this.brokerlist.get(brokername).removeAllReadingPorts();
	}

}
