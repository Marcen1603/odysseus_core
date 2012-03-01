package de.uniol.inf.is.odysseus.core.mep;

import de.uniol.inf.is.odysseus.core.mep.ParseException;

/**
 * 
 * @author Jonas Jacobi
 *
 */
public interface IExpressionParser {

	IExpression<?> parse(String expressionStr) throws ParseException;

}
