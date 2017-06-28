package de.uniol.inf.is.odysseus.server.keyvalue.predicate;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.expression.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

public class KeyValuePredicateBuilder<T extends KeyValueObject<?>> implements IExpressionBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		SDFExpression expression = new SDFExpression("", predicate, null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		KeyValuePredicate<T> pred = new KeyValuePredicate<>(expression, resolver.getSchema());
		return pred;
	}

	@Override
	public IExpression createExpression(IAttributeResolver resolver, String expression) {
		// FIXME: Implement method
		throw new UnsupportedOperationException();
	}
}