package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;

public class ParsedExpression extends ParsedAggregation {

	private static IExpressionParser expressionParser = CQLGenerator.injector.getInstance(IExpressionParser.class);
	
	private String expressionString;
	
	public ParsedExpression(SimpleSelect select, SelectExpression expression, String name) {
		super(select, expression, name);
		this.expressionString = "['" + expressionParser.parse(expression).toString() +"','" + name + "']";
	}
	
	public ParsedExpression(ParsedExpression parsedExpression) {
		super(parsedExpression);
		this.expressionString = parsedExpression.expressionString;
	}
	
	@Override
	public Type getType() {
		return Type.EXPRESSION;
	}
	
	@Override
	public String toString() {
		return expressionString;
	}
	
}
