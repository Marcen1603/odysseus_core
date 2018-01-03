package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;

public interface IAttributeParser {

	Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select);
	QueryAttribute[] computeProjectionAttributes(QueryAttribute[] list, SimpleSelect select,
			Attribute attribute, String attributename, String attributealias, String sourcename);
	String getExpressionName();
	String getAggregationName(String name);

}
