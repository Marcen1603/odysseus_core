package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public interface ISelectParser {

	void clear();
	void prepare(SimpleSelect select);
	String parse(SimpleSelect select);
	String parseWithPredicate(SimpleSelect select);
	String parseComplex(SimpleSelect left, SimpleSelect right, String operator);
	
}
