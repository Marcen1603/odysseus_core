package de.uniol.inf.is.odysseus.server.keyvalue.expression;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.server.keyvalue.predicate.KeyValuePredicate;

public class KeyValuePredicateBuilder<M extends IMetaAttribute> extends AbstractExpressionBuilder<KeyValueObject<M>, M> {

	@Override
	public IPredicate<KeyValueObject<M>> createPredicate(IAttributeResolver resolver, String predicate) {
		SDFExpression expression = new SDFExpression("", predicate, null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		return new KeyValuePredicate<>(expression, resolver.getSchema());
	}

	@Override
	public String getName() {
		return "KEYVALUEPREDICATE";
	}
	
	@Override
	public String getAliasName() {
		return KeyValueObject.class.getName();
	}


}