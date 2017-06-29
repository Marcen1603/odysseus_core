package de.uniol.inf.is.odysseus.server.keyvalue.expression;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.server.keyvalue.predicate.KeyValuePredicate;

public class KeyValuePredicateBuilder<T extends KeyValueObject<?>> extends AbstractExpressionBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		SDFExpression expression = new SDFExpression("", predicate, null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		KeyValuePredicate<T> pred = new KeyValuePredicate<>(expression, resolver.getSchema());
		return pred;
	}



}