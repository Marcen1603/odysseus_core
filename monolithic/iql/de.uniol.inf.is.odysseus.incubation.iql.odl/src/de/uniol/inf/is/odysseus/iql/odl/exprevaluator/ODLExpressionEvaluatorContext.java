package de.uniol.inf.is.odysseus.iql.odl.exprevaluator;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.AbstractIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.IIQLExpressionEvaluatorContext;

public class ODLExpressionEvaluatorContext extends AbstractIQLExpressionEvaluatorContext implements IODLExpressionEvaluatorContext{

	@Override
	public IIQLExpressionEvaluatorContext cleanCopy() {
		return new ODLExpressionEvaluatorContext();
	}

}
