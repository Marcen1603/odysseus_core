package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QueryAttributeOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QuerySourceOrder;

public interface IAttributeParser {

	Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select);
	String getExpressionName();
	String getAggregationName(String name);
	void registerAttributesFromPredicate(SimpleSelect select);
	QuerySourceOrder getSourceOrder();
	QueryAttributeOrder getAttributeOrder();
	Collection<QueryAggregate> getAggregates();
	void clear();

}
