package de.uniol.inf.is.odysseus.broker.dictionary;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.broker.transaction.TransactionType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerDictionaryEntry {
	private SDFAttributeList outputSchema;
	private SDFAttributeList tupleInputSchema;
	private SDFAttributeList tsInputSchema;
	private String brokerName;
	
	private TransactionType.Write[] writingTypes = new TransactionType.Write[0];
	
	
	public BrokerDictionaryEntry(String brokername){
		this.brokerName = brokername;		
	}
	
	public BrokerDictionaryEntry(String brokername, SDFAttributeList outputSchema, SDFAttributeList tupleInputSchema, SDFAttributeList tsInputSchema){
		this.brokerName = brokername;
		this.outputSchema = outputSchema;
		this.tupleInputSchema = tupleInputSchema;
		this.tsInputSchema = tsInputSchema;
	}
	
	public int addNewWriteTransaction(TransactionType.Write type){
		int addedToPort = writingTypes.length;
		writingTypes = Arrays.copyOf(writingTypes, writingTypes.length+2);
		writingTypes[addedToPort] = type;
		return addedToPort;
	}
	
	public TransactionType.Write getWriteType(int port){
		return writingTypes[port];
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
}
