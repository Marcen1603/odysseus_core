package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class SelectAOBuilder extends AbstractOperatorBuilder {

	private static final String PREDICATE = "PREDICATE";
	private PredicateParameter predicateParameter = new PredicateParameter(
			PREDICATE, REQUIREMENT.MANDATORY);

	public SelectAOBuilder() {
		super(1, 1);
		setParameters(predicateParameter);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SelectAO selectAO = new SelectAO();
		selectAO.setPredicate(predicateParameter.getValue());

		return selectAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
