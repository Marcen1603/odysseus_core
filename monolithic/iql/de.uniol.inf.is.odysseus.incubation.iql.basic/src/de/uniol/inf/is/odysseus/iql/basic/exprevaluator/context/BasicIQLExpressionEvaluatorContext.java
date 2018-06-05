package de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context;

public class BasicIQLExpressionEvaluatorContext extends AbstractIQLExpressionEvaluatorContext{

	@Override
	public IIQLExpressionEvaluatorContext cleanCopy() {
		return new BasicIQLExpressionEvaluatorContext();
	}

}
