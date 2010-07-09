package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.BufferAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.DirectParameter;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

public class BufferAOBuilder extends AbstractOperatorBuilder {

	DirectParameter<String> type = new DirectParameter<String>("TYPE",
			REQUIREMENT.MANDATORY);

	public BufferAOBuilder() {
		setParameters(type);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		BufferAO bufferAO = new BufferAO();
		bufferAO.setType(type.getValue());

		return bufferAO;
	}

}
