package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;

public interface IJoinParser {
	
	String buildJoin(Collection<Source> sources);
	String buildJoin(String[] srcs);
	boolean isJoinInQuery();
	void clear();

}
