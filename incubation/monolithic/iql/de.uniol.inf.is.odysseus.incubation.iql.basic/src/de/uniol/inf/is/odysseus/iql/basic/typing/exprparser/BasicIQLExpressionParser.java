package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.BasicIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;


public class BasicIQLExpressionParser extends AbstractIQLExpressionParser<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionParserContext>{

	@Inject
	public BasicIQLExpressionParser(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionParserContext context) {
		super(typeFactory, lookUp, context);
	}

}
