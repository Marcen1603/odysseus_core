package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context;

public class BasicIQLExpressionParserContext extends AbstractIQLExpressionParserContext{

	@Override
	public IIQLExpressionParserContext cleanCopy() {
		return new BasicIQLExpressionParserContext();
	}

}
