package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class FileSinkAOBuilder extends AbstractOperatorBuilder {

	private final DirectParameter<String> fileName = new DirectParameter<String>(
			"FILE", REQUIREMENT.MANDATORY);
	
	public FileSinkAOBuilder() {
		super(0, Integer.MAX_VALUE);
		setParameters(fileName);
	}

	private static final long serialVersionUID = 167513434840811081L;

	@Override
	protected boolean internalValidation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new FileSinkAO(this.fileName.getValue());
	}

}
