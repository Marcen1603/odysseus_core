package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.BufferAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class BufferAOBuilder extends AbstractOperatorBuilder {

	DirectParameter<String> type = new DirectParameter<String>("TYPE",
			REQUIREMENT.MANDATORY);

	public BufferAOBuilder() {
		super(1, 1);
		setParameters(type);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		BufferAO bufferAO = new BufferAO();
		bufferAO.setType(type.getValue());

		return bufferAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
