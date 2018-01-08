package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryExpression;

public interface IProjectionParser {

	Object[] parse(Collection<QueryExpression> expressions, String input); 
	String parse(SimpleSelect select, String operator);
	
}
