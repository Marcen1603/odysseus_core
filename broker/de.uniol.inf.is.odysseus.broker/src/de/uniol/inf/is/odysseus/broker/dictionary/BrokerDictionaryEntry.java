package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Represents the knowledge for a broker.  
 * 
 * @author Dennis Geesen
 */
public class BrokerDictionaryEntry {
	private SDFAttributeList schema = new SDFAttributeList();
	private SDFAttributeList queueSchema = new SDFAttributeList();
	private String brokerName;	
	
	private WriteTransaction[] writingTypes = new WriteTransaction[0];
	private ReadTransaction[] readingTypes = new ReadTransaction[0];
	private List<QueuePortMapping> portMappings = new ArrayList<QueuePortMapping>();
	
	
	/**
	 * Creates a new instance for the given name
	 * @param brokername Name of the broker
	 */
	public BrokerDictionaryEntry(String brokername){
		this.brokerName = brokername;			
	}
	
	/**
	 * Creates a new instance for the given name and schemas
	 * @param brokername Name of the broker
	 * @param schema Output and input schema of the broker
	 * @param queueSchema Schema of the queue 
	 */
	public BrokerDictionaryEntry(String brokername, SDFAttributeList schema, SDFAttributeList queueSchema){
		this.brokerName = brokername;
		this.schema = schema;		
		this.queueSchema = queueSchema;	
	}
	
	/**
	 * Adds a new writing transaction to the broker
	 * @param type The type of the transaction
	 * @return the assigned incoming port
	 */
	public int addNewWriteTransaction(WriteTransaction type){
		int addedToPort = writingTypes.length;
		writingTypes = Arrays.copyOf(writingTypes, writingTypes.length+1);
		writingTypes[addedToPort] = type;						
		return addedToPort;		
	}
	
	/**
	 * Adds a new reading transaction to the broker
	 * @param type The type of the transaction
	 * @return the assigned outgoing port
	 */
	public int addNewReadTransaction(ReadTransaction type){
		int addedToPort = readingTypes.length;
		readingTypes = Arrays.copyOf(readingTypes, readingTypes.length+1);
		readingTypes[addedToPort] = type;
		return addedToPort;
	}
	
	/**
	 * Gets the assigned type of the writing transaction for a given port 
	 * @param port The port
	 * @return Type of the Transaction
	 */
	public WriteTransaction getWriteType(int port){
		return writingTypes[port];
	}

	/**
	 * Gets the assigned type of the reading transaction for a given port	
	 * @param port The port
	 * @return Type of the Transaction
	 */
	public ReadTransaction getReadType(int port){
		return readingTypes[port];
	}		

	/**
	 * Gets the data output and input schema
	 * @return The schema
	 */
	public SDFAttributeList getSchema() {
		return schema;
	}

	/**
	 * Gets the queue input schema
	 * @return The schema
	 */
	public SDFAttributeList getQueueSchema() {
		return queueSchema;
	}

	/**
	 * Gets the name of the broker
	 * @return The given name
	 */
	public String getBrokerName() {
		return brokerName;
	}

	/**
	 * Sets the type for a writing transaction
	 * @param port The existing port
	 * @param type The new type of the transaction 
	 */
	public void setWriteType(int port, WriteTransaction type) {
		this.writingTypes[port] = type;		
	}
	
	/**
	 * Sets the type for a reading transaction
	 * @param port The existing port
	 * @param type The new type of the transaction
	 */
	public void setReadType(int port, ReadTransaction type) {
		this.readingTypes[port] = type;		
	}
	
	/**
	 * Returns all reading Transactions
	 * @return The transactions
	 */
	public ReadTransaction[] getReadingTransactions() {
		return this.readingTypes;
	}	
	
	/**
	 * Remove all reading Transactions
	 */
	public void removeAllReadingPorts(){		
		this.readingTypes = new ReadTransaction[0];
	}
	
	/**
	 * Adds a new port mapping
	 * @param portMapping The mapping
	 */
	public void addPortMapping(QueuePortMapping portMapping){
		this.portMappings.add(portMapping);
	}
	
	/**
	 * Delivers all port mappings
	 * @return The port mappings
	 */
	public List<QueuePortMapping> getPortMappings(){
		return this.portMappings;
	}
}
