package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.BatchProducerAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.IntegerParameter;
import de.uniol.inf.is.odysseus.parser.pql.ListParameter;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

public class BatchProducerBuilder extends AbstractOperatorBuilder {

	IntegerParameter invertedPriorityRatio = new IntegerParameter(
			"INVERTEDPRIORITYRATIO", REQUIREMENT.MANDATORY);
	ListParameter<BatchItem> batches = new ListParameter<BatchItem>(
			"BATCHES", REQUIREMENT.MANDATORY, new BatchParameter("BATCH",
					REQUIREMENT.MANDATORY));

	public BatchProducerBuilder() {
		setParameters(invertedPriorityRatio, batches);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 0);
		
		BatchProducerAO ao = new BatchProducerAO();
		ao.setInvertedPriorityRatio(invertedPriorityRatio.getValue());
		for(BatchItem batch : batches.getValue()){
			ao.addBatch(batch.size, batch.wait);
		}
		
		return ao;
	}

}
