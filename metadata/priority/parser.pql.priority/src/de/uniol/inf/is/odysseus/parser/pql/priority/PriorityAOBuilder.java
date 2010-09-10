package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class PriorityAOBuilder extends AbstractOperatorBuilder {

	// DirectParameter<Long> defaultPriority = new
	// DirectParameter<Long>("DEFAULT", REQUIREMENT.OPTIONAL);
	private PredicateParameter prioritize = new PredicateParameter(
			"PRIORITIZE", REQUIREMENT.MANDATORY);

	public PriorityAOBuilder() {
		super(1, 1);
		setParameters(prioritize);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ILogicalOperator createOperatorInternal() {
		PriorityAO<RelationalTuple<?>> priorityAO = new PriorityAO<RelationalTuple<?>>();
		priorityAO.setPriority((byte) 1,
				(IPredicate<? super RelationalTuple<?>>) prioritize.getValue());
		priorityAO.setDefaultPriority((byte) 0);

		return priorityAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}
}
