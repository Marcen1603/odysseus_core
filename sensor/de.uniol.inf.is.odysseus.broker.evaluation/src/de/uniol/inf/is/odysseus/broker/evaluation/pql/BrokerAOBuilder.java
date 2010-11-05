package de.uniol.inf.is.odysseus.broker.evaluation.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerAOBuilder extends AbstractOperatorBuilder {

	IParameter<String> nameAttribute = new DirectParameter<String>("NAME", REQUIREMENT.MANDATORY);
	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>("SCHEMA", REQUIREMENT.OPTIONAL, new CreateSDFAttributeParameter("ATTRIBUTE",
			REQUIREMENT.MANDATORY));
	private final IntegerParameter queuePort = new IntegerParameter("Q",
			REQUIREMENT.OPTIONAL);
	private final IntegerParameter dataPort = new IntegerParameter("O",
			REQUIREMENT.OPTIONAL);
	private final IntegerParameter inPort = new IntegerParameter("I",
			REQUIREMENT.OPTIONAL);

	public BrokerAOBuilder() {
		super(0, 2);
		setParameters(nameAttribute);
		setParameters(attributes);
		setParameters(inPort);
		setParameters(dataPort);
		setParameters(queuePort);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {

		String name = nameAttribute.getValue();

		// first occurrence?!
		createEntriesIfNecessary();

		
		int currentIn = getInPort();
		int currentOut = getOutPort();
		int currentQueue = getQueuePort();
		
		// create a logical op
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
		// and consume a writing port, because at least one input is
		// possible...		
		broker.setInPort(currentIn);
		broker.setOutPort(currentOut);
		broker.setQueuePort(currentQueue);

		// this is our mapping from queue-in to data-out, so we need a QPM:
		QueuePortMapping qpm = new QueuePortMapping(currentOut, currentQueue);
		BrokerDictionary.getInstance().addQueuePortMapping(broker.getName(), qpm);

		// so, reading and writing data are first continuous (cyclic will be
		// set during transform)
		// and queue is a timestamp-port
		BrokerDictionary.getInstance().setReadTypeForPort(name, currentOut, ReadTransaction.Continuous);
		BrokerDictionary.getInstance().setWriteTypeForPort(name, currentIn, WriteTransaction.Continuous);
		BrokerDictionary.getInstance().setWriteTypeForPort(name, currentQueue, WriteTransaction.Timestamp);				
		return broker;
	}

	private void createEntriesIfNecessary() {
		String name = nameAttribute.getValue();
		if (!BrokerDictionary.getInstance().brokerExists(name)) {
			if (super.getInputOperatorCount() > 0) {
				SDFAttributeList schema = super.getInputOperator(0).getOutputSchema();
				BrokerDictionary.getInstance().addBroker(name, schema, generateQueueSchema());
			} else {
				if (attributes.hasValue()) {
					List<SDFAttribute> attributeList = attributes.getValue();
					SDFAttributeList schema = new SDFAttributeList(attributeList);
					BrokerDictionary.getInstance().addBroker(name, schema, generateQueueSchema());
				} else {
					throw new RuntimeException("Attributes has to be set, if broker is used for the first time and there is no other input");
				}
			}
			BrokerDictionary.getInstance().setCurrentInputPort(name, 0);
			BrokerDictionary.getInstance().setCurrentOuputPort(name, 0);
		}
	}

	private SDFAttributeList generateQueueSchema() {
		SDFAttributeList queueSchema = new SDFAttributeList();
		queueSchema.add(new SDFAttribute("broker", "timestamp"));
		return queueSchema;
	}
	
	private int getInPort(){
		String name = nameAttribute.getValue();
		int current = BrokerDictionary.getInstance().getCurrentInputPort(name);
		if(inPort.hasValue()){
			int port = inPort.getValue();
			if(current<=port){
				BrokerDictionary.getInstance().setCurrentInputPort(name, port + 1);
			}
			current = port;
		}else{
			BrokerDictionary.getInstance().setCurrentInputPort(name, current + 1);
		}			
		return current;
	}

	private int getQueuePort(){
		String name = nameAttribute.getValue();
		int current = BrokerDictionary.getInstance().getCurrentInputPort(name);
		if(queuePort.hasValue()){
			int port = queuePort.getValue();
			if(current<=port){
				BrokerDictionary.getInstance().setCurrentInputPort(name, port + 1);
			}
			current = port;
		}else{
			BrokerDictionary.getInstance().setCurrentInputPort(name, current + 1);
		}			
		return current;
	}
	
	private int getOutPort(){
		String name = nameAttribute.getValue();
		int current = BrokerDictionary.getInstance().getCurrentOutputPort(name);
		if(dataPort.hasValue()){
			int port = dataPort.getValue();
			if(current<=port){
				BrokerDictionary.getInstance().setCurrentOuputPort(name, port + 1);
			}
			current = port;
		}else{
			BrokerDictionary.getInstance().setCurrentOuputPort(name, current + 1);
		}			
		return current;
	}

}
