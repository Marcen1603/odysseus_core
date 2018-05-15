package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;

public interface IJoinParser {
	
	String buildJoin(Collection<QuerySource> sources, SimpleSelect select);
	String buildJoin(String[] srcs);
	boolean isJoinInQuery();
	void clear();

}
