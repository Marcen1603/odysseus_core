package de.uniol.inf.is.odysseus.iql.qdl.exprevaluator;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.AbstractIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.IIQLExpressionEvaluatorContext;

public class QDLExpressionEvaluatorContext extends AbstractIQLExpressionEvaluatorContext implements IQDLExpressionEvaluatorContext{

	@Override
	public IIQLExpressionEvaluatorContext cleanCopy() {
		return new QDLExpressionEvaluatorContext();
	}

}
