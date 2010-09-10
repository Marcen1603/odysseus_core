package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.TestProducerAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class TestProducerPOBuilder extends AbstractOperatorBuilder {
	IntegerParameter invertedPriorityRatio = new IntegerParameter(
			"INVERTEDPRIORITYRATIO", REQUIREMENT.MANDATORY);
	ListParameter<BatchItem> parts = new ListParameter<BatchItem>("PARTS",
			REQUIREMENT.MANDATORY, new BatchParameter("BATCH",
					REQUIREMENT.MANDATORY));

	public TestProducerPOBuilder() {
		super(0, 0);
		setParameters(invertedPriorityRatio, parts);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		TestProducerAO ao = new TestProducerAO();
		ao.setInvertedPriorityRatio(invertedPriorityRatio.getValue());
		for (BatchItem batch : parts.getValue()) {
			ao.addTestPart(batch.size, batch.wait);
		}

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
