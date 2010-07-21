package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.SplitAO;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

public class SplitAOBuilder extends AbstractOperatorBuilder {

	ListParameter<IPredicate<?>> predicates = new ListParameter<IPredicate<?>>(
			"PREDICATES", REQUIREMENT.MANDATORY, new PredicateParameter(
					"PREDICATE", REQUIREMENT.MANDATORY));

	public SplitAOBuilder() {
		setParameters(predicates);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		SplitAO ao = new SplitAO();
		ao.setPredicates(predicates.getValue());

		return ao;
	}

}
