package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

import java.util.List;

public interface IPredicateParser {

	void clear();
	CharSequence parse(List<Expression> expressions, SimpleSelect select);
	CharSequence parsePredicateString(List<String> object);
	List<String> getPredicateStringList();

}
