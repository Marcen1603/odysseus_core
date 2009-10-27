package de.uniol.inf.is.odysseus.base.predicate;

import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public interface IPredicateBuilder {
	IPredicate<MetaAttributeContainer<?>> createPredicate(AbstractPredicate predicate, IAttributeResolver resolver);
}
