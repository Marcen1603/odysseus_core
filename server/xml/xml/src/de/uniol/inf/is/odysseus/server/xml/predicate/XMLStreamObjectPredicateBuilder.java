package de.uniol.inf.is.odysseus.server.xml.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;

public class XMLStreamObjectPredicateBuilder<T extends XMLStreamObject<?>> implements IPredicateBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		predicate = predicate.replaceAll("/", XMLStreamObject.SLASH_REPLACEMENT_STRING);
		SDFExpression expression = new SDFExpression("", predicate, null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		XMLStreamObjectPredicate<T> pred = new XMLStreamObjectPredicate<>(expression, resolver.getSchema());
		return pred;
	}
}