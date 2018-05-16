package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;

public interface IRenameParser {
	
	CharSequence buildRename(CharSequence input, SimpleSource simpleSource, SimpleSelect select, int selfJoin);
	Collection<String> getAliases();
	Collection<QuerySource> getSources();
	String parse(Collection<String> groupAttributes, String input, SimpleSelect select) throws PQLOperatorBuilderException;
	void setSources(Collection<QuerySource> sources);
	void clear();
	
}
