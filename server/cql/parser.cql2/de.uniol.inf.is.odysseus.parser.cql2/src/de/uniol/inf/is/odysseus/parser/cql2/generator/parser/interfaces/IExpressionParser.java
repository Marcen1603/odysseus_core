package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;

public interface IExpressionParser {

	String parse(SelectExpression expression);
	Collection<SelectExpression> extractSelectExpressionsFromArgument(Collection<SelectArgument> args);
	
}
