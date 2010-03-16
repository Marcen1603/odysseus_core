package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerDictionaryEntry {
	private SDFAttributeList outputSchema;
	private SDFAttributeList tupleInputSchema;
	private SDFAttributeList tsInputSchema;
	private String brokerName;
	
	private WriteTransaction[] writingTypes = new WriteTransaction[0];
	private ReadTransaction[] readingTypes = new ReadTransaction[0];
	
	
	public BrokerDictionaryEntry(String brokername){
		this.brokerName = brokername;			
	}
	
	public BrokerDictionaryEntry(String brokername, SDFAttributeList outputSchema, SDFAttributeList tupleInputSchema, SDFAttributeList tsInputSchema){
		this.brokerName = brokername;
		this.outputSchema = outputSchema;
		this.tupleInputSchema = tupleInputSchema;
		this.tsInputSchema = tsInputSchema;		
	}
	
	public int addNewWriteTransaction(WriteTransaction type){
		int addedToPort = writingTypes.length;
		writingTypes = Arrays.copyOf(writingTypes, writingTypes.length+2);
		writingTypes[addedToPort] = type;
		return addedToPort;
	}
	
	public WriteTransaction getWriteType(int port){
		return writingTypes[port];
	}

	public int addNewReadTransaction(ReadTransaction type){
		int addedToPort = readingTypes.length;
		readingTypes = Arrays.copyOf(readingTypes, readingTypes.length+1);
		readingTypes[addedToPort] = type;
		return addedToPort;
	}
	
	public ReadTransaction getReadType(int port){
		return readingTypes[port];
	}
	
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;
	}

	public SDFAttributeList getTupleInputSchema() {
		return tupleInputSchema;
	}

	public void setTupleInputSchema(SDFAttributeList tupleInputSchema) {
		this.tupleInputSchema = tupleInputSchema;
	}

	public SDFAttributeList getTsInputSchema() {
		return tsInputSchema;
	}

	public void setTsInputSchema(SDFAttributeList tsInputSchema) {
		this.tsInputSchema = tsInputSchema;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setWriteType(int port, WriteTransaction type) {
		this.writingTypes[port] = type;		
	}
	
	public void setReadType(int port, ReadTransaction type) {
		this.readingTypes[port] = type;		
	}

	public void resetAllWritingPorts(WriteTransaction type) {
		Arrays.fill(this.writingTypes, type);		
	}

	public void resetAllReadingPorts(ReadTransaction type) {
		Arrays.fill(this.readingTypes, type);		
	}

	public ReadTransaction[] getReadingPorts() {
		return this.readingTypes;
	}	
	
	public void removeAllReadingPorts(){		
		this.readingTypes = new ReadTransaction[0];
	}
}
