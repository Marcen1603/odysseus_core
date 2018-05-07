package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public interface ISelectParser {

	void clear();
	void prepare(SimpleSelect select, NestedSource innerSelect);
	void parse(SimpleSelect select);
	void parseSingleSelect(SimpleSelect select);
	String parseWithPredicate(SimpleSelect select);
	String parseComplex(SimpleSelect left, SimpleSelect right, String operator);
	void fooor(SimpleSelect select);
	
}
