package de.uniol.inf.is.odysseus.broker.evaluation.pql;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.benchmark.BenchmarkAO;
import de.uniol.inf.is.odysseus.broker.evaluation.benchmark.BufferAO;
import de.uniol.inf.is.odysseus.broker.evaluation.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.UpdateEvaluationAO;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.evaluation.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FreqCycleBuilder extends AbstractOperatorBuilder {

	private final IntegerParameter numbers = new IntegerParameter("NUM",
			REQUIREMENT.MANDATORY);
	
	public FreqCycleBuilder() {
		super(1, 2);		
		setParameters(numbers);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		String name = "context";
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);	
		
		List<UpdateEvaluationAO> updaters = new ArrayList<UpdateEvaluationAO>();
		int dataInPort = 0;		
		int dataOutPort = 1;
		
		ILogicalOperator inputLeft = super.getInputOperator(0);
		SDFAttributeList schema = inputLeft.getOutputSchema();
		
		
		BrokerDictionary.getInstance().addBroker(name, schema, generateQueueSchema());
		broker.setSchema(schema);
		broker.setQueueSchema(generateQueueSchema());
		
		BrokerDictionary.getInstance().setCurrentInputPort(name, 0);
		BrokerDictionary.getInstance().setCurrentOuputPort(name, 0);
		
		
		BrokerDictionary.getInstance().setReadTypeForPort(name, 0, ReadTransaction.Continuous);
		
		for(int i=1;i<=super.getInputOperatorCount();i++){
			int procTime = 50000000;
			if(i==1){
				procTime = 100000000;
			}
			
			ILogicalOperator input = super.getInputOperator(i-1);
			input.unsubscribeSink(broker, 0, 0, input.getOutputSchema());
			int queueInPort = dataInPort +1;
			UpdateEvaluationAO updater = new UpdateEvaluationAO(i);			
			// data in connection
			//updater.subscribeSink(broker, dataInPort, 0, schema);
			BenchmarkAO benchmarker = new BenchmarkAO(procTime, 1.0);
			BufferAO buffer = new BufferAO();
			updater.subscribeSink(buffer, 0, 0, schema);
			buffer.subscribeSink(benchmarker, 0, 0, schema);
			//updater.subscribeSink(benchmarker, 0, 0, schema);
			benchmarker.subscribeSink(broker, dataInPort, 0, schema);
			//queue in connection
			updater.subscribeSink(broker, queueInPort, 1, generateQueueSchema());
			//data out (= updater in) connection			
			broker.subscribeSink(updater, 0, dataOutPort, schema);
			// sensor data for updater
//			BufferAO bufferIn = new BufferAO();
//			input.subscribeSink(bufferIn, 0, 0, schema);
//			bufferIn.subscribeSink(updater, 1, 0, schema);
			input.subscribeSink(updater, 1, 0, schema);
			
				
			updaters.add(updater);
			BrokerDictionary.getInstance().setCurrentInputPort(name, queueInPort+1);
			BrokerDictionary.getInstance().setCurrentOuputPort(name, dataOutPort+1);
			BrokerDictionary.getInstance().setReadTypeForPort(name, dataOutPort, ReadTransaction.Continuous);
			BrokerDictionary.getInstance().setWriteTypeForPort(name, dataInPort, WriteTransaction.Continuous);
			BrokerDictionary.getInstance().setWriteTypeForPort(name, queueInPort, WriteTransaction.Timestamp);	
			BrokerDictionary.getInstance().addQueuePortMapping(name, new QueuePortMapping(dataOutPort, queueInPort));
			
			dataInPort= queueInPort+1;			
			dataOutPort++;
			
		}		
		return broker;
	}

	
	private SDFAttributeList generateQueueSchema() {
		SDFAttributeList queueSchema = new SDFAttributeList();
		queueSchema.add(new SDFAttribute("broker", "timestamp"));
		return queueSchema;
	}

}
