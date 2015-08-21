package de.uniol.inf.is.odysseus.iql.qdl.exprevaluator;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.factory.IQDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.QDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLExpressionEvaluator extends AbstractIQLExpressionEvaluator<IQDLTypeFactory, IQDLLookUp, IQDLExpressionEvaluatorContext, IQDLTypeUtils, QDLTypeExtensionsFactory> implements IQDLExpressionEvaluator{

	@Inject
	public QDLExpressionEvaluator(IQDLTypeFactory typeFactory, IQDLLookUp lookUp, IQDLExpressionEvaluatorContext context, IQDLTypeUtils typeUtils, QDLTypeExtensionsFactory typeExtensionsFactory) {
		super(typeFactory, lookUp, context, typeUtils, typeExtensionsFactory);
	}

}
