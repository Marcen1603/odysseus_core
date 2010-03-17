package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
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
			
	public void addBroker(String name, SDFAttributeList schema, SDFAttributeList queueSchema){
		if(!brokerExists(name)){			
			BrokerDictionaryEntry entry = new BrokerDictionaryEntry(name, schema, queueSchema);
			this.brokerlist.put(name, entry);
		}
	}
		
	public void addNewTransactionMapping(String brokername, int readingPort, int queueWritingPort){
		this.brokerlist.get(brokername).addPortMapping(new QueuePortMapping(readingPort, queueWritingPort));
	}
	
	public int addNewTransaction(String brokername, WriteTransaction type){
		return this.brokerlist.get(brokername).addNewWriteTransaction(type);
	}
	
	public int addNewTransaction(String brokername, ReadTransaction type){
		return this.brokerlist.get(brokername).addNewReadTransaction(type);
	}

	public SDFAttributeList getSchema(String brokername) {		
		return this.brokerlist.get(brokername).getSchema();
	}
	
	public SDFAttributeList getQueueSchema(String brokername){
		return this.brokerlist.get(brokername).getQueueSchema();
	}

	public WriteTransaction getWriteTypeForPort(String brokername, int port) {		
		return this.brokerlist.get(brokername).getWriteType(port);
	}
	
	public ReadTransaction getReadTypeForPort(String brokername, int port) {		
		return this.brokerlist.get(brokername).getReadType(port);
	}

	public ReadTransaction[] getReadingTransactions(String brokername) {
		return this.brokerlist.get(brokername).getReadingTransactions();
	}
	
	public List<QueuePortMapping> getQueuePortMappings(String brokername){
		return this.brokerlist.get(brokername).getPortMappings();
	}

	public void setReadTypeForPort(String brokername, int outgoingPort, ReadTransaction type) {
		this.brokerlist.get(brokername).setReadType(outgoingPort, type);
		
	}

	public void setWriteTypeForPort(String brokername, int incomingPort, WriteTransaction type) {
		this.brokerlist.get(brokername).setWriteType(incomingPort, type); 		
	}

}
