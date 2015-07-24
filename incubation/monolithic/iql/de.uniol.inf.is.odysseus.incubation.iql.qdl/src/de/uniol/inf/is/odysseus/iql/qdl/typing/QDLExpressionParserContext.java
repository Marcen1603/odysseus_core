package de.uniol.inf.is.odysseus.iql.qdl.typing;

import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.AbstractIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.IIQLExpressionParserContext;

public class QDLExpressionParserContext extends AbstractIQLExpressionParserContext{

	@Override
	public IIQLExpressionParserContext cleanCopy() {
		return new QDLExpressionParserContext();
	}

}
