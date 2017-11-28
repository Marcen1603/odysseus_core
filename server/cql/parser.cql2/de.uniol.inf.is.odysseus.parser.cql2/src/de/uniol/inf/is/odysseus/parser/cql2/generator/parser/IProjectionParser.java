package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public interface IProjectionParser {

	Object[] parse(Collection<SelectExpression> expressions, String input); 
	String parse(SimpleSelect select, String operator);
	
}
