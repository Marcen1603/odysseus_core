/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;

/**
 * Represents the knowledge for a broker.  
 * 
 * @author Dennis Geesen, Andre Bolles
 */
public class BrokerDictionaryEntry {
	
	// TODO: DIESE KLASSE MUSS ÜBERARBEITET WERDEN!!
	/** The data schema. */
	private SDFSchemaExtended schema = null;
	
	/** The queue schema. */
	// TODO: diese Variable wird gar nicht wirklich verwendet! Ist immer leere Liste/null
	final private SDFSchema queueSchema;
	
	/** The broker name. */
	private String brokerName;	
	
	/** The writing types. */
//	private WriteTransaction[] writingTypes = new WriteTransaction[0];
	private HashMap<Integer, WriteTransaction> writingTypes = new HashMap<Integer, WriteTransaction>();
	
	/** The reading types. */
//	private ReadTransaction[] readingTypes = new ReadTransaction[0];
	private HashMap<Integer, ReadTransaction> readingTypes = new HashMap<Integer, ReadTransaction>();
	
	/** The port mappings. */
	private List<QueuePortMapping> portMappings = new ArrayList<QueuePortMapping>();
	
	/**
	 * The logical plan, containing this broker.
	 */
	private BrokerAO logicalPlan; 
	
	public BrokerAO getLogicalPlan() {
		return logicalPlan;
	}

	public void setLogicalPlan(BrokerAO logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	/**
	 * A broker can be created by more than one statement. Therefore, we have
	 * to store the current input port position. If the broker has no input connections,
	 * the current input port is 0. If there is one connection the current input port is 1
	 * and so on. This field is only used for parsing and
	 * adding additional query plans.
	 */
	private int currentInputPort;
	
	public int getCurrentInputPort() {
		return currentInputPort;
	}

	public void setCurrentInputPort(int currentInputPort) {
		this.currentInputPort = currentInputPort;
	}

	public int getCurrentOutputPort() {
		return currentOutputPort;
	}

	public void setCurrentOutputPort(int currentOutputPort) {
		this.currentOutputPort = currentOutputPort;
	}

	/***
	 * A broker can be created by more than one statement. Therefore, we have
	 * to store the current output port position. If there is no reading access to
	 * the broker the current output port is 0. If there is one reading access, the
	 * current output port is 1 and so on. This field is only used for parsing and
	 * adding additional query plans.
	 */
	private int currentOutputPort;
	
	/**
	 * Creates a new instance for the given name.
	 *
	 * @param brokername Name of the broker
	 */
	public BrokerDictionaryEntry(String brokername){
		this.brokerName = brokername;			
		queueSchema = null;
	}
	
	
	/**
	 * Creates a new instance for the given name and schemas.
	 *
	 * @param brokername Name of the broker
	 * @param schema Output and input schema of the broker
	 * @param queueSchema Schema of the queue
	 */
	public BrokerDictionaryEntry(String brokername, SDFSchemaExtended schema, SDFSchema queueSchema){
		this.brokerName = brokername;
		this.schema = schema;		
		this.queueSchema = queueSchema;	
	}
	
	/**
	 * Adds a new writing transaction to the broker.
	 *
	 * @param type The type of the transaction
	 * @return the assigned incoming port
	 * @deprecated cyclic logical plans are created completely before transformation
	 * from now on. So this method is not used any more.
	 */
	public int addNewWriteTransaction(WriteTransaction type){
//		int addedToPort = writingTypes.length;
//		writingTypes = Arrays.copyOf(writingTypes, writingTypes.length+1);
//		writingTypes[addedToPort] = type;						
//		return addedToPort;
		throw new RuntimeException("Transactions of a Broker have to be set directly now.");
	}
	
	/**
	 * Adds a new reading transaction to the broker.
	 *
	 * @param type The type of the transaction
	 * @return the assigned outgoing port
	 * @deprecated cyclic logical plans are created completely before transformation
	 * from now on. So this method is not used any more.
	 */
	public int addNewReadTransaction(ReadTransaction type){
//		int addedToPort = readingTypes.length;
//		readingTypes = Arrays.copyOf(readingTypes, readingTypes.length+1);
//		readingTypes[addedToPort] = type;
//		return addedToPort;
		throw new RuntimeException("Transactions of a Broker have to be set directly now.");
	}
	
	/**
	 * Gets the assigned type of the writing transaction for a given port.
	 *
	 * @param port The port
	 * @return Type of the Transaction
	 */
	public WriteTransaction getWriteType(int port){
		return writingTypes.get(port);
	}

	/**
	 * Gets the assigned type of the reading transaction for a given port.
	 *
	 * @param port The port
	 * @return Type of the Transaction
	 */
	public ReadTransaction getReadType(int port){
		return readingTypes.get(port);
	}		

	/**
	 * Gets the data output and input schema.
	 *
	 * @return The schema
	 */
	public SDFSchemaExtended getSchema() {
		return schema;
	}

	/**
	 * Gets the queue input schema.
	 *
	 * @return The schema
	 */
	public SDFSchema getQueueSchema() {
		return queueSchema;
	}

	/**
	 * Gets the name of the broker.
	 *
	 * @return The given name
	 */
	public String getBrokerName() {
		return brokerName;
	}

	/**
	 * Sets the type for a writing transaction.
	 *
	 * @param port The existing port
	 * @param type The new type of the transaction
	 */
	public void setWriteType(int port, WriteTransaction type) {
		this.writingTypes.put(port, type);		
	}
	
	/**
	 * Sets the type for a reading transaction.
	 *
	 * @param port The existing port
	 * @param type The new type of the transaction
	 */
	public void setReadType(int port, ReadTransaction type) {
		this.readingTypes.put(port,type);		
	}
	
	/**
	 * Returns all reading Transactions.
	 *
	 * @return The transactions
	 */
	public HashMap<Integer, ReadTransaction> getReadingTransactions() {
		return this.readingTypes;
	}	
	
	/**
	 * Remove all reading Transactions.
	 */
	public void removeAllReadingPorts(){		
		this.readingTypes = new HashMap<Integer, ReadTransaction>();
	}
	
	/**
	 * Adds a new port mapping.
	 *
	 * @param portMapping The mapping
	 */
	public void addPortMapping(QueuePortMapping portMapping){
		this.portMappings.add(portMapping);
	}
	
	/**
	 * Delivers all port mappings.
	 *
	 * @return The port mappings
	 */
	public List<QueuePortMapping> getPortMappings(){
		return this.portMappings;
	}
}
