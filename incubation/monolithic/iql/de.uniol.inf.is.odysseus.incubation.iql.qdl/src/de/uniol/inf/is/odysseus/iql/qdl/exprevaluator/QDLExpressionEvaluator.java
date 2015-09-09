package de.uniol.inf.is.odysseus.iql.qdl.exprevaluator;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.QDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLExpressionEvaluator extends AbstractIQLExpressionEvaluator<IQDLTypeDictionary, IQDLLookUp, IQDLExpressionEvaluatorContext, IQDLTypeUtils, QDLTypeExtensionsDictionary> implements IQDLExpressionEvaluator{

	@Inject
	public QDLExpressionEvaluator(IQDLTypeDictionary typeDictionary, IQDLLookUp lookUp, IQDLExpressionEvaluatorContext context, IQDLTypeUtils typeUtils, QDLTypeExtensionsDictionary typeExtensionsDictionary) {
		super(typeDictionary, lookUp, context, typeUtils, typeExtensionsDictionary);
	}
	
	@Override
	public TypeResult getType(IQLSuperExpression expr, IQDLExpressionEvaluatorContext context) {
		QDLQuery query = EcoreUtil2.getContainerOfType(expr, QDLQuery.class);
		if (query != null) {
			return new TypeResult(lookUp.getSuperType(expr));
		} else {
			return super.getType(expr, context);
		}
	}
	

}
