package de.uniol.inf.is.odysseus.core.mep;

import de.uniol.inf.is.odysseus.core.mep.ParseException;

public interface IExpressionParser {

	IExpression<?> parse(String expressionStr) throws ParseException;

}
