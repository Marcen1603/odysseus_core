package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * The {@link BrokerDictionary} contains several information for each broker.
 * This class is singleton.
 * 
 * Each information is stored in a {@link BrokerDictionaryEntry}.
 * 
 * @author Dennis Geesen
 * 
 */
public class BrokerDictionary {
	
	/** The instance of this class. */
	private static BrokerDictionary instance;
	
	/** The brokerlist contains the entries for each broker. */
	private Map<String, BrokerDictionaryEntry> brokerlist = new HashMap<String, BrokerDictionaryEntry>();

	/**
	 * Instantiates a new broker dictionary. 
	 */
	private BrokerDictionary() {
	}

	/**
	 * Gets the single instance of {@link BrokerDictionary}.
	 *
	 * @return single instance of {@link BrokerDictionary}
	 */
	public static synchronized BrokerDictionary getInstance() {
		if (instance == null) {
			instance = new BrokerDictionary();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	/**
	 * Checks whether a broker exists.
	 *
	 * @param name the name of the broker
	 * @return true, if a broker exists
	 */
	public boolean brokerExists(String name) {
		return brokerlist.keySet().contains(name);
	}

	/**
	 * Adds a new broker.
	 *
	 * @param name the name of the broker
	 * @param schema the data schema of the broker
	 * @param queueSchema the queue schema of the broker
	 */
	public void addBroker(String name, SDFAttributeListExtended schema, SDFAttributeList queueSchema) {
		if (!brokerExists(name)) {
			BrokerDictionaryEntry entry = new BrokerDictionaryEntry(name, schema, queueSchema);
			this.brokerlist.put(name, entry);
		}
	}

	/**
	 * Adds the new transaction mapping.
	 * A transaction mapping is a {@link QueuePortMapping} 
	 * and assigns a reading data stream to a writing queue stream.
	 * The transaction control will coordinate the data stream by tuples from the assigned queue stream.
	 *
	 * @param brokername the name of the broker
	 * @param readingPort the port for the reading data stream
	 * @param queueWritingPort the port for the writing queue stream
	 */
	public void addNewTransactionMapping(String brokername, int readingPort, int queueWritingPort) {
		this.brokerlist.get(brokername).addPortMapping(new QueuePortMapping(readingPort, queueWritingPort));
	}

	/**
	 * Adds a new writing transaction.
	 *
	 * @param brokername the name of the broker
	 * @param type the transaction type
	 * @return the given port for the transaction
	 */
	public int addNewTransaction(String brokername, WriteTransaction type) {
		return this.brokerlist.get(brokername).addNewWriteTransaction(type);
	}

	/**
	 * Adds a new reading transaction.
	 *
	 * @param brokername the name of the broker
	 * @param type the transaction type
	 * @return the given port for the transaction
	 */
	public int addNewTransaction(String brokername, ReadTransaction type) {
		return this.brokerlist.get(brokername).addNewReadTransaction(type);
	}

	/**
	 * Gets the data schema for a given broker.
	 *
	 * @param brokername the name of the broker
	 * @return the data schema
	 */
	public SDFAttributeListExtended getSchema(String brokername) {
		return this.brokerlist.get(brokername).getSchema();
	}

	/**
	 * Gets the queue schema for a given broker.
	 *
	 * @param brokername the name of the broker
	 * @return the queue schema
	 */
	public SDFAttributeList getQueueSchema(String brokername) {
		return this.brokerlist.get(brokername).getQueueSchema();
	}

	/**
	 * Gets the transaction type of a writing port.
	 *
	 * @param brokername the name of the broker
	 * @param port the port
	 * @return the write type
	 */
	public WriteTransaction getWriteTypeForPort(String brokername, int port) {
		return this.brokerlist.get(brokername).getWriteType(port);
	}

	/**
	 * Gets the transaction type of a reading port.
	 *
	 * @param brokername the brokername
	 * @param port the port
	 * @return the read type
	 */
	public ReadTransaction getReadTypeForPort(String brokername, int port) {
		return this.brokerlist.get(brokername).getReadType(port);
	}

	/**
	 * Gets all reading transactions.
	 *
	 * @param brokername the name of the broker
	 * @return an array of all reading transactions
	 */
	public HashMap<Integer, ReadTransaction> getReadingTransactions(String brokername) {
		return this.brokerlist.get(brokername).getReadingTransactions();
	}

	/**
	 * Gets all {@link QueuePortMapping}.
	 *
	 * @param brokername the name of the broker
	 * @return the queue port mappings
	 */
	public List<QueuePortMapping> getQueuePortMappings(String brokername) {
		return this.brokerlist.get(brokername).getPortMappings();
	}

	/**
	 * Adds a new {@link QueuePortMapping} to a broker.
	 *
	 * @param brokername the name of the broker
	 * @param map the {@link QueuePortMapping}
	 */
	public void addQueuePortMapping(String brokername, QueuePortMapping map) {
		this.brokerlist.get(brokername).addPortMapping(map);
	}

	/**
	 * Sets the read type for a given port.
	 *
	 * @param brokername the name of the broker
	 * @param outgoingPort the port to change
	 * @param type the new transaction type
	 */
	public void setReadTypeForPort(String brokername, int outgoingPort, ReadTransaction type) {
		this.brokerlist.get(brokername).setReadType(outgoingPort, type);

	}

	/**
	 * Sets the write type for a given port.
	 *
	 * @param brokername the name of the broker
	 * @param incomingPort the port to change
	 * @param type the new transaction type
	 */
	public void setWriteTypeForPort(String brokername, int incomingPort, WriteTransaction type) {
		this.brokerlist.get(brokername).setWriteType(incomingPort, type);
	}

	/**
	 * Removes all reading ports.
	 *
	 * @param brokername the name of the broker
	 */
	public void clearAllReadingPorts(String brokername) {
		this.brokerlist.get(brokername).removeAllReadingPorts();
	}
	
	public int getCurrentInputPort(String brokername){
		return this.brokerlist.get(brokername).getCurrentInputPort();
	}
	
	public void setCurrentInputPort(String brokername, int curIn){
		this.brokerlist.get(brokername).setCurrentInputPort(curIn);
	}
	
	public int getCurrentOutputPort(String brokername){
		return this.brokerlist.get(brokername).getCurrentOutputPort();
	}
	
	public void setCurrentOuputPort(String brokername, int curOut){
		this.brokerlist.get(brokername).setCurrentOutputPort(curOut);
	}

	public void setLogicalPlan(String brokername, BrokerAO brokerAO){
		this.brokerlist.get(brokername).setLogicalPlan(brokerAO);
	}
	
	public BrokerAO getLogicalPlan(String brokername){
		return this.brokerlist.get(brokername).getLogicalPlan();
	}
	
	public Map<String, BrokerDictionaryEntry> getLogicalPlans(){
		return this.brokerlist;
	}
}
