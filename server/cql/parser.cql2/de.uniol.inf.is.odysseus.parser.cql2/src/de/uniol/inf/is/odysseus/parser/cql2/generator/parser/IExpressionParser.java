package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;

public interface IExpressionParser {

	String parse(SelectExpression expression);
	
}
