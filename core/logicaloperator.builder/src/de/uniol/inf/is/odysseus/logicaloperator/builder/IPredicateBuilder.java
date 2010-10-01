package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public interface IPredicateBuilder {
	public IPredicate<?> createPredicate(IAttributeResolver resolver,
			String predicate);
}
