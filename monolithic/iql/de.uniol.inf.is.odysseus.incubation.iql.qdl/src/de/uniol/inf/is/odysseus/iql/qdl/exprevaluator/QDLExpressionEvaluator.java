package de.uniol.inf.is.odysseus.iql.qdl.exprevaluator;

import javax.inject.Inject;


import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.QDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLExpressionEvaluator extends AbstractIQLExpressionEvaluator<IQDLTypeDictionary, IQDLLookUp, IQDLExpressionEvaluatorContext, IQDLTypeUtils, QDLTypeExtensionsDictionary> implements IQDLExpressionEvaluator{

	@Inject
	public QDLExpressionEvaluator(IQDLTypeDictionary typeDictionary, IQDLLookUp lookUp, IQDLExpressionEvaluatorContext context, IQDLTypeUtils typeUtils, QDLTypeExtensionsDictionary typeExtensionsDictionary) {
		super(typeDictionary, lookUp, context, typeUtils, typeExtensionsDictionary);
	}

}
