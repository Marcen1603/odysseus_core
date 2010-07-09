package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.TestProducerAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.IntegerParameter;
import de.uniol.inf.is.odysseus.parser.pql.ListParameter;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

public class TestProducerPOBuilder extends AbstractOperatorBuilder {
	IntegerParameter invertedPriorityRatio = new IntegerParameter(
			"INVERTEDPRIORITYRATIO", REQUIREMENT.MANDATORY);
	ListParameter<BatchItem> parts = new ListParameter<BatchItem>("PARTS",
			REQUIREMENT.MANDATORY, new BatchParameter("BATCH",
					REQUIREMENT.MANDATORY));

	public TestProducerPOBuilder() {
		setParameters(invertedPriorityRatio, parts);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 0);

		TestProducerAO ao = new TestProducerAO();
		ao.setInvertedPriorityRatio(invertedPriorityRatio.getValue());
		for (BatchItem batch : parts.getValue()) {
			ao.addTestPart(batch.size, batch.wait);
		}

		return ao;
	}

}
