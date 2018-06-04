package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;

public interface IAggregationParser {
	
	Object[] parse(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<QuerySource> srcs, SimpleSelect select) throws PQLOperatorBuilderException;

}
