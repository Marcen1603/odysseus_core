package de.uniol.inf.is.odysseus.iql.odl.exprevaluator;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLExpressionEvaluator extends AbstractIQLExpressionEvaluator<ODLTypeFactory, ODLLookUp, ODLExpressionEvaluatorContext, ODLTypeUtils, ODLTypeExtensionsFactory>{

	@Inject
	public ODLExpressionEvaluator(ODLTypeFactory typeFactory, ODLLookUp lookUp, ODLExpressionEvaluatorContext context,ODLTypeUtils typeUtils, ODLTypeExtensionsFactory typeExtensionsFactory) {
		super(typeFactory, lookUp, context, typeUtils, typeExtensionsFactory);
	}
	
	@Override
	public TypeResult getType(IQLSuperExpression expr, ODLExpressionEvaluatorContext context) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (operator != null) {
			return getSuperType(expr);
		} else {
			return super.getType(expr, context);
		}
	}
	
	@Override
	public TypeResult getSuperType(EObject obj) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(obj, ODLOperator.class);
		if (operator != null) {
			return new TypeResult(typeUtils.createTypeRef(AbstractPipe.class, typeFactory.getSystemResourceSet()));
		} else {
			return super.getSuperType(obj);
		}
	}
	
}
