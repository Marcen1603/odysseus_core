package de.uniol.inf.is.odysseus.iql.basic.exprevaluator;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.BasicIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLExpressionEvaluator extends AbstractIQLExpressionEvaluator<BasicIQLTypeDictionary, BasicIQLLookUp, BasicIQLExpressionEvaluatorContext, BasicIQLTypeUtils, BasicIQLTypeExtensionsDictionary>{

	@Inject
	public BasicIQLExpressionEvaluator(BasicIQLTypeDictionary typeDictionary,BasicIQLLookUp lookUp, BasicIQLExpressionEvaluatorContext context, BasicIQLTypeUtils typeUtils, BasicIQLTypeExtensionsDictionary typeExtensionsDictionary) {
		super(typeDictionary, lookUp, context,typeUtils, typeExtensionsDictionary);
	}

}
