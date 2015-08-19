package de.uniol.inf.is.odysseus.iql.basic.exprevaluator;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.BasicIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLExpressionEvaluator extends AbstractIQLExpressionEvaluator<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionEvaluatorContext, BasicIQLTypeUtils, BasicIQLTypeExtensionsFactory>{

	@Inject
	public BasicIQLExpressionEvaluator(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionEvaluatorContext context, BasicIQLTypeUtils typeUtils, BasicIQLTypeExtensionsFactory extensionsFactory) {
		super(typeFactory, lookUp, context,typeUtils, extensionsFactory);
	}

}
