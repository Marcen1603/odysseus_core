package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;

public interface IAggregationParser {
	
	Object[] parse(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<Source> srcs, SimpleSelect select);

}
