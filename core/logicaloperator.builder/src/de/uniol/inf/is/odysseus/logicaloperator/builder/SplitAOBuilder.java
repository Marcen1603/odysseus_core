package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SplitAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

public class SplitAOBuilder extends AbstractOperatorBuilder {

	ListParameter<IPredicate<?>> predicates = new ListParameter<IPredicate<?>>(
			"PREDICATES", REQUIREMENT.MANDATORY, new PredicateParameter(
					"PREDICATE", REQUIREMENT.MANDATORY));

	public SplitAOBuilder() {
		super(1, 1);
		setParameters(predicates);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SplitAO ao = new SplitAO();
		ao.setPredicates(predicates.getValue());

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
