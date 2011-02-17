package de.uniol.inf.is.odysseus.datamining.state.builder;

import de.uniol.inf.is.odysseus.datamining.state.logicaloperator.StateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;

public class StateAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = 3210465592145893383L;

	private BooleanParameter openParameter = new BooleanParameter("OPEN", REQUIREMENT.OPTIONAL);
	private IntegerParameter openAtParam = new IntegerParameter("OPENAT", REQUIREMENT.OPTIONAL);

	public StateAOBuilder() {
		super(2, 2);
		setParameters(openParameter, openAtParam);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		boolean open = true;
		if (openParameter.hasValue()) {
			open = openParameter.getValue();
		}
		int openAt = 10000;
		if (openAtParam.hasValue()) {
			openAt = openAtParam.getValue();
		}
		return new StateAO(open, openAt);
	}

}
