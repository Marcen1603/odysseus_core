package de.uniol.inf.is.odysseus.iql.qdl.typing;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.AbstractIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;

public class QDLExpressionParser extends AbstractIQLExpressionParser<QDLTypeFactory, QDLLookUp, QDLExpressionParserContext, QDLTypeUtils>{

	@Inject
	public QDLExpressionParser(QDLTypeFactory typeFactory, QDLLookUp lookUp, QDLExpressionParserContext context, QDLTypeUtils typeUtils) {
		super(typeFactory, lookUp, context, typeUtils);
	}

}
