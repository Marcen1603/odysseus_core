package de.uniol.inf.is.odysseus.keyvalue.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;

public class KeyValuePredicateBuilder implements IPredicateBuilder {
	
	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		SDFExpression expression = new SDFExpression("", predicate, resolver, MEP.getInstance());
		KeyValuePredicate pred = new KeyValuePredicate(expression);
		return pred;
	}
}