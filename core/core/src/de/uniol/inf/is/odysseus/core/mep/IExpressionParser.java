package de.uniol.inf.is.odysseus.core.mep;

import de.uniol.inf.is.odysseus.core.mep.ParseException;

/**
 * This interface encapsulates different expression parsers
 * 
 * @author Marco Grawunder
 *
 */
public interface IExpressionParser {

	/**
	 * Parse a String expression
	 * 
	 * @param expressionStr The expression to parse
	 * @return an IExpression object that contains the parsed expression
	 * @throws ParseException 
	 */
	IExpression<?> parse(String expressionStr) throws ParseException;

}
