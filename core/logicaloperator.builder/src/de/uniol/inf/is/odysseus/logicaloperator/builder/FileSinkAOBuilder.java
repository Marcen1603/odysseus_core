package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class FileSinkAOBuilder extends AbstractOperatorBuilder {

	private final DirectParameter<String> fileName = new DirectParameter<String>(
			"FILE", REQUIREMENT.MANDATORY);
	private final DirectParameter<String> sinktype = new DirectParameter<String>(
			"FILETYPE", REQUIREMENT.OPTIONAL); 
	private final DirectParameter<Long> writeAfterElements = new DirectParameter<Long>("CACHESIZE", REQUIREMENT.OPTIONAL);
	
	public FileSinkAOBuilder() {
		super(0, Integer.MAX_VALUE);
		setParameters(fileName, sinktype, writeAfterElements);
	}

	private static final long serialVersionUID = 167513434840811081L;

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new FileSinkAO(this.fileName.getValue(), this.sinktype.getValue(),
				this.writeAfterElements.getValue()!=null?this.writeAfterElements.getValue():-1);
	}

}
