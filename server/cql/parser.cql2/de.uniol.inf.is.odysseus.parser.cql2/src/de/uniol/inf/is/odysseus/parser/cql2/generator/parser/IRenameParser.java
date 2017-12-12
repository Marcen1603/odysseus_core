package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;

public interface IRenameParser {
	
	CharSequence buildRename(CharSequence input, SimpleSource simpleSource, int selfJoin);
	Collection<String> getAliases();
	Collection<Source> getSources();
	String parse(Collection<String> groupAttributes, String input);
	void setSources(Collection<Source> sources);
	void clear();
	
}
