package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractOperatorParser {

	static Collection<AbstractOperatorParser> parsers = new ArrayList<>();
	
	AbstractOperatorParser() {
		
		
		
	}

	abstract String parse(Object... args);

}
