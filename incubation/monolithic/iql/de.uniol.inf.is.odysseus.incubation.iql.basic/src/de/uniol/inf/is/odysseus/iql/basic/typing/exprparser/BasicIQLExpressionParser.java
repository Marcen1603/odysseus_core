package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.BasicIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLExpressionParser extends AbstractIQLExpressionParser<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionParserContext, BasicIQLTypeUtils, BasicIQLTypeExtensionsFactory>{

	@Inject
	public BasicIQLExpressionParser(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionParserContext context, BasicIQLTypeUtils typeUtils, BasicIQLTypeExtensionsFactory extensionsFactory) {
		super(typeFactory, lookUp, context,typeUtils, extensionsFactory);
	}

}
