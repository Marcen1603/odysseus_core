package de.uniol.inf.is.odysseus.iql.qdl.exprevaluator;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLExpressionEvaluator extends AbstractIQLExpressionEvaluator<QDLTypeFactory, QDLLookUp, QDLExpressionEvaluatorContext, QDLTypeUtils, QDLTypeExtensionsFactory>{

	@Inject
	public QDLExpressionEvaluator(QDLTypeFactory typeFactory, QDLLookUp lookUp, QDLExpressionEvaluatorContext context, QDLTypeUtils typeUtils, QDLTypeExtensionsFactory typeExtensionsFactory) {
		super(typeFactory, lookUp, context, typeUtils, typeExtensionsFactory);
	}

}
