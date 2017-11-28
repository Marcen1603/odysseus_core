package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

import java.util.Collection;
import java.util.Map;

public interface IExistenceParser {

	String parse();
	void register(SimpleSelect input, String select);
	Map<String, String> getOperator(int i);
	Collection<Map<String, String>> getOperators();
	
}
