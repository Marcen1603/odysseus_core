package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class PriorityPredicateBuilder implements IPredicateBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver,
			String predicate) {
		return new PriorityPredicate();
	}

}
