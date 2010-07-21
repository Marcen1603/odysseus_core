package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public interface IPredicateBuilder {
	public IPredicate<?> createPredicate(IAttributeResolver resolver,
			String predicate);
}
