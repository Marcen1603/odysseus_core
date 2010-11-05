package de.uniol.inf.is.odysseus.broker.evaluation.pql;

import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.UpdateEvaluationAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;

public class UpdateEvaluationAOBuilder extends AbstractOperatorBuilder {	
	
	
	public UpdateEvaluationAOBuilder() {
		super(2,2);		
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		UpdateEvaluationAO evalAO = new UpdateEvaluationAO();		
		return evalAO;
	}

}
