package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.benchmarker.impl.BatchProducerAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class BatchProducerBuilder extends AbstractOperatorBuilder {

	IntegerParameter invertedPriorityRatio = new IntegerParameter(
			"INVERTEDPRIORITYRATIO", REQUIREMENT.MANDATORY);
	ListParameter<BatchItem> batches = new ListParameter<BatchItem>("BATCHES",
			REQUIREMENT.MANDATORY, new BatchParameter("BATCH",
					REQUIREMENT.MANDATORY));

	public BatchProducerBuilder() {
		super(0, 0);
		setParameters(invertedPriorityRatio, batches);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		BatchProducerAO ao = new BatchProducerAO();
		ao.setInvertedPriorityRatio(invertedPriorityRatio.getValue());
		for (BatchItem batch : batches.getValue()) {
			ao.addBatch(batch.size, batch.wait);
		}

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
