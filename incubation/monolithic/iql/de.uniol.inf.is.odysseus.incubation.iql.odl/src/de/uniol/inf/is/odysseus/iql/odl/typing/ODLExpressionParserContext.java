package de.uniol.inf.is.odysseus.iql.odl.typing;

import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.AbstractIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.IIQLExpressionParserContext;

public class ODLExpressionParserContext extends AbstractIQLExpressionParserContext{

	@Override
	public IIQLExpressionParserContext cleanCopy() {
		return new ODLExpressionParserContext();
	}

}
