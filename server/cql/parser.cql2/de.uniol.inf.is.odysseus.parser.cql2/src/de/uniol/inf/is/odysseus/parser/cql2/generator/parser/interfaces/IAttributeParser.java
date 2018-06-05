package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryExpression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QueryAttributeOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QuerySourceOrder;

public interface IAttributeParser {

	Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select);
	String getExpressionName();
	String getAggregationName(String name);
	void registerAttributesFromPredicate(SimpleSelect select) ;
	QuerySourceOrder getSourceOrder();
	QueryAttributeOrder getAttributeOrder();
	Collection<QueryAggregate> getAggregates();
	void clear();
	Collection<QueryExpression> getExpressions();
	Collection<Pair<Source, String>> computeSourceCandidates(Attribute attribute, Collection<Source> sources);

}
