package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;

public interface IRenameParser {
	
	CharSequence buildRename(CharSequence input, SimpleSource simpleSource, SimpleSelect select, int selfJoin);
	Collection<String> getAliases();
	Collection<Source> getSources();
	String parse(Collection<String> groupAttributes, String input, SimpleSelect select);
	void setSources(Collection<Source> sources);
	void clear();
	
}
