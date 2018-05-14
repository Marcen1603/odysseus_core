package de.uniol.inf.is.odysseus.server.xml.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XMLStreamObjectPredicateBuilder<M extends IMetaAttribute> extends AbstractExpressionBuilder<XMLStreamObject<M>,M> {

	@Override
	public IPredicate<XMLStreamObject<M>> createPredicate(IAttributeResolver resolver, String predicate) {
		predicate = predicate.replaceAll("/", XMLStreamObject.SLASH_REPLACEMENT_STRING);
		predicate = predicate.replaceAll("@", XMLStreamObject.AT_REPLACEMENT_STRING);

		SDFExpression expression = new SDFExpression("", predicate, null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		IPredicate<XMLStreamObject<M>> pred = new XMLStreamObjectPredicate<>(expression, resolver.getSchema());
		return pred;
	}
	
}