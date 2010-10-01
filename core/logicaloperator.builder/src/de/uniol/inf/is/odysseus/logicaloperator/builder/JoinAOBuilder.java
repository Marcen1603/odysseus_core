package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class JoinAOBuilder extends AbstractOperatorBuilder {

	private static final String PREDICATE = "PREDICATE";
	private PredicateParameter predicateParameter = new PredicateParameter(
			PREDICATE, REQUIREMENT.OPTIONAL);

	public JoinAOBuilder() {
		super(2, 2);
		setParameters(predicateParameter);
	}

	protected ILogicalOperator createOperatorInternal() {
		JoinAO joinAO = new JoinAO();
		if(predicateParameter.hasValue()){
			joinAO.setPredicate(predicateParameter.getValue());
		}
		return joinAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
