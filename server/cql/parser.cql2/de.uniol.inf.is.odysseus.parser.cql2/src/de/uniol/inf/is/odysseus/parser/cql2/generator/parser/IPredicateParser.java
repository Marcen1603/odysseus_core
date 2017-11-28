package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import java.util.List;

public interface IPredicateParser {

	void clear();
	CharSequence parse(List<Expression> expressions);
	CharSequence parsePredicateString(List<String> object);
	List<String> getPredicateStringList();

}
